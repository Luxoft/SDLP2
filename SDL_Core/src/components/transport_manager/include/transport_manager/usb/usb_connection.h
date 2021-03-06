/**
 * \file usb_connection.h
 * \brief UsbConnection class header file.
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

#ifndef SRC_COMPONENTS_TRANSPORT_MANAGER_INCLUDE_TRANSPORT_MANAGER_USB_CONNECTION_H_
#define SRC_COMPONENTS_TRANSPORT_MANAGER_INCLUDE_TRANSPORT_MANAGER_USB_CONNECTION_H_

#include <pthread.h>

#include "transport_manager/transport_adapter/transport_adapter_controller.h"
#include "transport_manager/transport_adapter/connection.h"

namespace transport_manager {
namespace transport_adapter {

class UsbConnection : public Connection {
 public:
  UsbConnection(const DeviceUID& device_uid,
                const ApplicationHandle& app_handle,
                TransportAdapterController* controller, libusb_device* device);

  bool Init();

  virtual ~UsbConnection();
 protected:
  virtual TransportAdapter::Error SendData(RawMessageSptr message);
  virtual TransportAdapter::Error Disconnect();
 private:

  friend void InTransferCallback(struct libusb_transfer*);
  friend void OutTransferCallback(struct libusb_transfer*);
  bool FindEndpoints();
  void PopOutMessage();
  bool PostInTransfer();
  bool PostOutTransfer();
  void OnInTransfer(struct libusb_transfer*);
  void OnOutTransfer(struct libusb_transfer*);
  void CheckAllTransfersComplete();

  const DeviceUID device_uid_;
  const ApplicationHandle app_handle_;
  TransportAdapterController* controller_;
  libusb_device* libusb_device_;
  libusb_device_handle* device_handle_;
  uint8_t in_endpoint_;
  uint16_t in_endpoint_max_packet_size_;
  uint8_t out_endpoint_;
  uint16_t out_endpoint_max_packet_size_;
  unsigned char* in_buffer_;
  libusb_transfer* in_transfer_;
  libusb_transfer* out_transfer_;

  std::list<RawMessageSptr> out_messages_;
  RawMessageSptr current_out_message_;
  pthread_mutex_t out_messages_mutex_;
  size_t bytes_sent_;
  bool disconnecting_;
  bool waiting_in_transfer_cancel_;
  bool waiting_out_transfer_cancel_;
};

}  // namespace transport_adapter
}  // namespace transport_manager

#endif // SRC_COMPONENTS_TRANSPORT_MANAGER_INCLUDE_TRANSPORT_MANAGER_USB_CONNECTION_H_
