/**
 * \file transport_adapter_listener_impl.h
 * \brief TransportAdapterListenerImpl class header file.
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

#ifndef SRC_COMPONENTS_TRANSPORT_MANAGER_INCLUDE_TRANSPORT_MANAGER_TRANSPORT_ADAPTER_TRANSPORT_ADAPTER_LISTENER_IMPL_H_
#define SRC_COMPONENTS_TRANSPORT_MANAGER_INCLUDE_TRANSPORT_MANAGER_TRANSPORT_ADAPTER_TRANSPORT_ADAPTER_LISTENER_IMPL_H_

#include "transport_manager/transport_adapter/transport_adapter_listener.h"
#include "transport_manager/transport_adapter/transport_adapter.h"
#include "utils/logger.h"

using transport_manager::transport_adapter::TransportAdapter;
using transport_manager::transport_adapter::TransportAdapterSptr;

namespace transport_manager {

/**
 * @typedef Type definition of iterator to container(vector) that holds smart pointers to the device adapters.
 */
typedef std::vector<TransportAdapterSptr>::const_iterator AdapterIterator;

class TransportManagerImpl;

/**
 * @brief Implementation of TransportAdapterListener class.
 */
class TransportAdapterListenerImpl :
    public transport_adapter::TransportAdapterListener {
 public:

  /**
   * @enum Available types of events.
   */
  enum EventTypeEnum {
    ON_SEARCH_DONE = 0,
    ON_SEARCH_FAIL,
    ON_DEVICE_LIST_UPDATED,
    ON_CONNECT_DONE,
    ON_CONNECT_FAIL,
    ON_DISCONNECT_DONE,
    ON_DISCONNECT_FAIL,
    ON_SEND_DONE,
    ON_SEND_FAIL,
    ON_RECEIVED_DONE,
    ON_RECEIVED_FAIL,
    ON_COMMUNICATION_ERROR,
    ON_UNEXPECTED_DISCONNECT
  };

  /**
   * @brief Constructor.
   *
   * @param tm Pointer to the transport manager implementation class.
   */
  explicit TransportAdapterListenerImpl(
      transport_manager::TransportManagerImpl *tm);

  /**
   * @brief Dectructor.
   */
  virtual ~TransportAdapterListenerImpl();

  /**
   * @brief Search specified device adapter in the container of shared pointers to device adapters to be sure it is available,
   * launch event ON_SEARCH_DONE in transport manager.
   *
   * @param transport_adapter Pointer to the device adapter.
   */
  virtual void OnSearchDeviceDone(const TransportAdapter* transport_adapter);

  /**
   * @brief Search specified device adapter in the container of shared pointers to device adapters to be sure it is available, create search device error,
   * launch event ON_SEARCH_FAIL in transport manager.
   *
   * @param transport_adapter Pointer to the device adapter.
   * @param error Error class with information about possible reason of search device failure.
   */
  virtual void OnSearchDeviceFailed(const TransportAdapter* transport_adapter,
                                    const SearchDeviceError& error);

  /**
   * @brief Passes notification to TransportManagerImpl
   *
   * @param transport_adapter Transport adapter that sent notification
   */
  virtual void OnDeviceListUpdated(const TransportAdapter* transport_adapter);

  /**
   * @brief Search specified device adapter in the container of shared pointers to device adapters to be sure it is available,
   * launch event ON_CONNECT_DONE in transport manager.
   *
   * @param device_adater Pointer to the device adapter.
   * @param device_handle Device unique identifier.
   * @param app_id Handle of application.
   */
  virtual void OnConnectDone(const TransportAdapter* transport_adapter,
                             const DeviceUID& device_handle,
                             const ApplicationHandle& app_id);

  /**
   * @brief Search specified device adapter in the container of shared pointers to device adapters to be sure it is available,
   * launch event ON_CONNECT_FAIL in transport manager.
   *
   * @param transport_adapter Pointer to the device adapter.
   * @param device Device unique identifier.
   * @param app_id Handle of application.
   * @param error Error class with information about possible reason of connect failure.
   */
  virtual void OnConnectFailed(const TransportAdapter* transport_adapter,
                               const DeviceUID& device,
                               const ApplicationHandle& app_id,
                               const ConnectError& error);

  /**
   * @brief
   *
   * @param transport_adapter Pointer to the device adapter.
   * @param device_handle Device unique identifier.
   * @param app_handle Handle of application.
   */
  virtual void OnConnectRequested(const TransportAdapter* transport_adapter,
                                  const DeviceUID& device_handle,
                                  const ApplicationHandle& app_handle);

  /**
   * @brief Search specified device adapter in the container of shared pointers to device adapters to be sure it is available,
   * launch event ON_UNEXPECTED_DISCONNECT in transport manager.
   *
   * @param transport_adapter Pointer to the device adapter.
   * @param device Device unique identifier.
   * @param app_id Handle of application.
   * @param error Error class with information about possible reason of unexpected Disconnect.
   */
  virtual void OnUnexpectedDisconnect(const TransportAdapter* transport_adapter,
                                      const DeviceUID& device,
                                      const ApplicationHandle& app_id,
                                      const CommunicationError& error);

  /**
   * @brief Search specified device adapter in the container of shared pointers to device adapters to be sure it is available,
   * launch event ON_DISCONNECT_DONE in transport manager.
   *
   * @param transport_adapter Pointer to the device adapter.
   * @param devcie_id Device unique identifier.
   * @param app_id Handle of application.
   */
  virtual void OnDisconnectDone(const TransportAdapter* transport_adapter,
                                const DeviceUID& device_id,
                                const ApplicationHandle& app_id);

  /**
   * @brief Search specified device adapter in the container of shared pointers to device adapters to be sure it is available, create Disconnect error,
   * launch event ON_DISCONNECT_FAIL in transport manager.
   *
   * @param transport_adapter Pointer to the device adapter.
   * @param device Device unique identifier.
   * @param app_id Handle of application.
   * @param error Error class with information about possible reason of Disconnect failure.
   */
  virtual void OnDisconnectFailed(const TransportAdapter* transport_adapter,
                                  const DeviceUID& device,
                                  const ApplicationHandle& app_id,
                                  const DisconnectError& error);

  /**
   * @brief
   *
   * @param transport_adapter Pointer to the device adapter.
   * @param devic Device unique identifier.
   */
  virtual void OnDisconnectDeviceDone(
      const transport_adapter::TransportAdapter* transport_adapter,
      const DeviceUID& devic);

  /**
   * @brief
   *
   * @param transport_adapter Pointer to the device adapter.
   * @param device Device unique identifier.
   * @param error Error class with information about possible reason of Disconnect from device failure.
   */
  virtual void OnDisconnectDeviceFailed(
      const transport_adapter::TransportAdapter* transport_adapter,
      const DeviceUID& device, const DisconnectDeviceError& error);

  /**
   * @brief Search specified device adapter in the container of shared pointers to device adapters to be sure it is available, create error,
   * launch event ON_RECEIVED_DONE in transport manager.
   *
   * @param transport_adapter Pointer to the device adapter.
   * @param devcie Device unique identifier.
   * @param app_id Handle of application.
   * @param data_container Smart pointer to the raw message.
   */
  virtual void OnDataReceiveDone(
      const transport_adapter::TransportAdapter* transport_adapter,
      const DeviceUID& device, const ApplicationHandle& app_id,
      const RawMessageSptr data_container);

  /**
   * @brief Search specified device adapter in the container of shared pointers to device adapters to be sure it is available, create data receive error,
   * launch event ON_RECEIVED_DONE in transport manager.
   *
   * @param transport_adapter Pointer to the device adapter.
   * @param device Device unique identifier.
   * @param app_id Handle of application.
   * @param error Error class with information about possible reason of data receive failure.
   */
  virtual void OnDataReceiveFailed(
      const transport_adapter::TransportAdapter* transport_adapter,
      const DeviceUID& device, const ApplicationHandle& app_id,
      const DataReceiveError& error);

  /**
   * @brief Search specified device adapter in the container of shared pointers to device adapters to be sure it is available, create error,
   * launch event ON_SEND_DONE in transport manager.
   *
   * @param transport_adapter Pointer to the device adapter.
   * @param device Device unique identifier.
   * @param app_id Handle of application.
   * @param data_container Smart pointer to the raw message.
   */
  virtual void OnDataSendDone(const TransportAdapter* transport_adapter,
                              const DeviceUID& device,
                              const ApplicationHandle& app_id,
                              const RawMessageSptr data_container);

  /**
   * @brief Search specified device adapter in the container of shared pointers to device adapters to be sure it is available, create data send error,
   * launch event ON_SEND_FAIL in transport manager.
   */
  virtual void OnDataSendFailed(const TransportAdapter* transport_adapter,
                                const DeviceUID& device,
                                const ApplicationHandle& app_id,
                                const RawMessageSptr data_container,
                                const DataSendError& error);

  /**
   * @brief Search specified device adapter in the container of shared pointers to device adapters to be sure it is available, create error,
   * launch event ON_COMMUNICATION_ERROR in transport manager.
   *
   * @param transport_adapter Pointer to the device adapter.
   * @param device Device unique identifier.
   * @param app_id Handle of application.
   */
  virtual void OnCommunicationError(
      const transport_adapter::TransportAdapter* transport_adapter,
      const DeviceUID& device, const ApplicationHandle& app_id);

  /**
   * @brief Search specified device adapter in the container of shared pointers to device adapters to be sure it is available.
   *
   * @param transport_adapter Pointer to the device adapter.
   * @param it Iterator to container(vector) that holds smart pointers to the device adapters.
   *
   * @return true - smart pointer to the device adapter in the container.
   * false - smart pointer to the device adapter not in the container.
   */
  inline bool FindSharedPtr(const TransportAdapter* transport_adapter,
                            AdapterIterator &it);

 private:
  /**
   * \brief For logging.
   */
  static log4cxx::LoggerPtr logger_;
  TransportManagerImpl *transport_manager_impl_;

};
}  // namespace transport_manager

#endif // SRC_COMPONENTS_TRANSPORT_MANAGER_INCLUDE_TRANSPORT_MANAGER_transport_adapter_transport_adapter_LISTENER_IMPL_H
