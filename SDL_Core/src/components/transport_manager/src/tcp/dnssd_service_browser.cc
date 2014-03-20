/**
 * \file dnssd_service_browser.cc
 * \brief DnssdServiceBrowser class source file.
 *
 * Copyright (c) 2013, Ford Motor Company
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following
 * disclaimer in the documentation and/or other materials provided with the
 * distribution.
 *
 * Neither the name of the Ford Motor Company nor the names of its contributors
 * may be used to endorse or promote products derived from this software
 * without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

#include <algorithm>
#include <map>

#include "transport_manager/transport_adapter/transport_adapter_impl.h"
#include "transport_manager/tcp/tcp_device.h"
#include "transport_manager/tcp/dnssd_service_browser.h"

namespace transport_manager {

namespace transport_adapter {

bool operator==(const DnssdServiceRecord& a, const DnssdServiceRecord& b) {
  return a.name == b.name && a.type == b.type && a.interface == b.interface
      && a.protocol == b.protocol && a.domain_name == b.domain_name;
}

void DnssdServiceBrowser::Terminate() {
  if (0 != avahi_threaded_poll_)
    avahi_threaded_poll_stop(avahi_threaded_poll_);
  if (0 != avahi_service_browser_)
    avahi_service_browser_free(avahi_service_browser_);
  if (0 != avahi_client_)
    avahi_client_free(avahi_client_);
  if (0 != avahi_threaded_poll_)
    avahi_threaded_poll_free(avahi_threaded_poll_);
}

bool DnssdServiceBrowser::IsInitialised() const {
  return initialised_;
}

DnssdServiceBrowser::DnssdServiceBrowser(TransportAdapterController* controller)
    : controller_(controller),
      avahi_service_browser_(0),
      avahi_threaded_poll_(0),
      avahi_client_(0),
      service_records_(),
      scan_requests_(0),
      services_to_be_resolved_(0),
      mutex_(),
      initialised_(false) {
  pthread_mutex_init(&mutex_, 0);
}

DnssdServiceBrowser::~DnssdServiceBrowser() {
  pthread_mutex_destroy(&mutex_);
}

void DnssdServiceBrowser::OnClientConnected() {
  initialised_ = true;
  LOG4CXX_ERROR(logger_, "AvahiClient ready");
}

void DnssdServiceBrowser::OnClientFailure() {
  const int avahi_errno = avahi_client_errno(avahi_client_);
  if (avahi_errno == AVAHI_ERR_DISCONNECTED) {
    LOG4CXX_INFO(logger_, "AvahiClient disconnected");
    CreateAvahiClientAndBrowser();
  } else {
    LOG4CXX_ERROR(logger_,
                  "AvahiClient failure: " << avahi_strerror(avahi_errno));
  }
}

void AvahiClientCallback(AvahiClient *avahi_client,
                         AvahiClientState avahi_client_state, void* data) {
  DnssdServiceBrowser* dnssd_service_browser =
      static_cast<DnssdServiceBrowser*>(data);

  switch (avahi_client_state) {
    case AVAHI_CLIENT_S_RUNNING:
      dnssd_service_browser->OnClientConnected();
      break;
    case AVAHI_CLIENT_FAILURE:
      dnssd_service_browser->OnClientFailure();
      break;
  }
}

void AvahiServiceBrowserCallback(AvahiServiceBrowser *avahi_service_browser,
                                 AvahiIfIndex interface, AvahiProtocol protocol,
                                 AvahiBrowserEvent event, const char *name,
                                 const char *type, const char *domain,
                                 AvahiLookupResultFlags flags, void* data) {
  DnssdServiceBrowser* dnssd_service_browser =
      static_cast<DnssdServiceBrowser*>(data);

  int avahi_errno;
  switch (event) {
    case AVAHI_BROWSER_FAILURE:
      avahi_errno = avahi_client_errno(
          avahi_service_browser_get_client(avahi_service_browser));
      LOG4CXX_ERROR(
          logger_,
          "AvahiServiceBrowser failure: " << avahi_strerror(avahi_errno));
      break;

    case AVAHI_BROWSER_NEW:
      dnssd_service_browser->AddService(interface, protocol, name, type,
                                        domain);
      break;

    case AVAHI_BROWSER_REMOVE:
      dnssd_service_browser->RemoveService(interface, protocol, name, type,
                                           domain);
      break;

    case AVAHI_BROWSER_ALL_FOR_NOW:
    case AVAHI_BROWSER_CACHE_EXHAUSTED:
      break;
  }
}

void DnssdServiceBrowser::ServiceResolved(
    const DnssdServiceRecord& service_record) {
  pthread_mutex_lock(&mutex_);
  ServiceRecords::iterator service_record_it = std::find(
      service_records_.begin(), service_records_.end(), service_record);
  if (service_record_it != service_records_.end()) {
    *service_record_it = service_record;
  }
  ServiceResolveFinished();
  pthread_mutex_unlock(&mutex_);
}

void DnssdServiceBrowser::ServiceResolveFailed(
    const DnssdServiceRecord& service_record) {
  LOG4CXX_ERROR(logger_,
                "AvahiServiceResolver failure for: " << service_record.name);
  pthread_mutex_lock(&mutex_);
  ServiceRecords::iterator service_record_it = std::find(
      service_records_.begin(), service_records_.end(), service_record);
  if (service_record_it != service_records_.end()) {
    service_records_.erase(service_record_it);
  }
  ServiceResolveFinished();
  pthread_mutex_unlock(&mutex_);
}

void DnssdServiceBrowser::ServiceResolveFinished() {
  if (0 == --services_to_be_resolved_) {
    DeviceVector device_vector = PrepareDeviceVector();
    for (int i = 0; i < scan_requests_; ++i) {
      OnSearchDone(device_vector);
    }
    scan_requests_ = 0;
  }
}

void DnssdServiceBrowser::OnSearchDone(const DeviceVector& device_vector) {
  controller_->SearchDeviceDone(device_vector);
}

void AvahiServiceResolverCallback(AvahiServiceResolver* avahi_service_resolver,
                                  AvahiIfIndex interface,
                                  AvahiProtocol protocol,
                                  AvahiResolverEvent event, const char* name,
                                  const char* type, const char* domain,
                                  const char* host_name,
                                  const AvahiAddress* avahi_address,
                                  uint16_t port, AvahiStringList* txt,
                                  AvahiLookupResultFlags flags, void *data) {
  DnssdServiceBrowser* dnssd_service_browser =
      static_cast<DnssdServiceBrowser*>(data);

  DnssdServiceRecord service_record;
  service_record.interface = interface;
  service_record.protocol = protocol;
  service_record.domain_name = domain;
  service_record.host_name = host_name;
  service_record.name = name;
  service_record.type = type;
  switch (event) {
    case AVAHI_RESOLVER_FOUND:
      service_record.addr = avahi_address->data.ipv4.address;
      service_record.port = port;
      dnssd_service_browser->ServiceResolved(service_record);
      break;
    case AVAHI_RESOLVER_FAILURE:
      dnssd_service_browser->ServiceResolveFailed(service_record);
      break;
  }

  avahi_service_resolver_free(avahi_service_resolver);
}

TransportAdapter::Error DnssdServiceBrowser::CreateAvahiClientAndBrowser() {
  if (0 != avahi_service_browser_)
    avahi_service_browser_free(avahi_service_browser_);
  if (0 != avahi_client_)
    avahi_client_free(avahi_client_);

  int avahi_error;
  avahi_client_ = avahi_client_new(
      avahi_threaded_poll_get(avahi_threaded_poll_), AVAHI_CLIENT_NO_FAIL,
      AvahiClientCallback, this, &avahi_error);
  if (0 == avahi_client_) {
    LOG4CXX_ERROR(
        logger_,
        "Failed to create AvahiClient: " << avahi_strerror(avahi_error));
    return TransportAdapter::FAIL;
  }

  pthread_mutex_lock(&mutex_);
  service_records_.clear();
  pthread_mutex_unlock(&mutex_);

  avahi_service_browser_ = avahi_service_browser_new(
      avahi_client_, AVAHI_IF_UNSPEC, /* TODO use only required iface */
      AVAHI_PROTO_INET, DNSSD_DEFAULT_SERVICE_TYPE, NULL, /* use default domain */
      static_cast<AvahiLookupFlags>(0), AvahiServiceBrowserCallback, this);

  return TransportAdapter::OK;
}

TransportAdapter::Error DnssdServiceBrowser::Init() {
  avahi_threaded_poll_ = avahi_threaded_poll_new();
  if (0 == avahi_threaded_poll_) {
    LOG4CXX_ERROR(logger_, "Failed to create AvahiThreadedPoll");
    return TransportAdapter::FAIL;
  }

  const TransportAdapter::Error err = CreateAvahiClientAndBrowser();
  if (err != TransportAdapter::OK) {
    return err;
  }

  const int poll_start_status = avahi_threaded_poll_start(avahi_threaded_poll_);
  if (poll_start_status != 0) {
    LOG4CXX_ERROR(logger_, "Failed to start AvahiThreadedPoll");
    return TransportAdapter::FAIL;
  }

  return TransportAdapter::OK;
}

TransportAdapter::Error DnssdServiceBrowser::Scan() {
  pthread_mutex_lock(&mutex_);
  if (0 == services_to_be_resolved_) {
    DeviceVector device_vector = PrepareDeviceVector();
    OnSearchDone(device_vector);
  } else {
    ++scan_requests_;
  }
  pthread_mutex_unlock(&mutex_);
  return TransportAdapter::OK;
}

void DnssdServiceBrowser::AddService(AvahiIfIndex interface,
                                     AvahiProtocol protocol, const char* name,
                                     const char* type, const char* domain) {
  DnssdServiceRecord record;
  record.interface = interface;
  record.protocol = protocol;
  record.domain_name = domain;
  record.name = name;
  record.type = type;

  pthread_mutex_lock(&mutex_);
  if (service_records_.end()
      == std::find(service_records_.begin(), service_records_.end(), record)) {
    service_records_.push_back(record);
    ++services_to_be_resolved_;
    AvahiServiceResolver* avahi_service_resolver = avahi_service_resolver_new(
        avahi_client_, interface, protocol, name, type, domain,
        AVAHI_PROTO_INET, static_cast<AvahiLookupFlags>(0),
        AvahiServiceResolverCallback, this);
  }
  pthread_mutex_unlock(&mutex_);
}

void DnssdServiceBrowser::RemoveService(AvahiIfIndex interface,
                                        AvahiProtocol protocol,
                                        const char* name, const char* type,
                                        const char* domain) {
  DnssdServiceRecord record;
  record.interface = interface;
  record.protocol = protocol;
  record.name = name;
  record.type = type;
  record.domain_name = domain;

  pthread_mutex_lock(&mutex_);
  service_records_.erase(
      std::remove(service_records_.begin(), service_records_.end(), record),
      service_records_.end());
  pthread_mutex_unlock(&mutex_);
}

DeviceVector DnssdServiceBrowser::PrepareDeviceVector() const {
  std::map<uint32_t, TcpDevice*> devices;
  for (ServiceRecords::const_iterator it = service_records_.begin();
      it != service_records_.end(); ++it) {
    const DnssdServiceRecord& service_record = *it;
    if (devices[service_record.addr] == 0) {
      devices[service_record.addr] = new TcpDevice(service_record.addr,
                                                   service_record.host_name);
    }
    if (devices[service_record.addr] != 0) {
      devices[service_record.addr]->AddDiscoveredApplication(
          service_record.port);
    }
  }
  DeviceVector device_vector;
  device_vector.reserve(devices.size());
  for (std::map<uint32_t, TcpDevice*>::const_iterator it = devices.begin();
      it != devices.end(); ++it) {
    device_vector.push_back(DeviceSptr(it->second));
  }
  return device_vector;
}

}  // namespace
}  // namespace

