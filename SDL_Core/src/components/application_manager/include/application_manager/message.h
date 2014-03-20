/**
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

#ifndef SRC_COMPONENTS_APPLICATION_MANAGER_INCLUDE_APPLICATION_MANAGER_MESSAGE_H_
#define SRC_COMPONENTS_APPLICATION_MANAGER_INCLUDE_APPLICATION_MANAGER_MESSAGE_H_

#include <string>
#include <vector>

#include "utils/shared_ptr.h"

namespace application_manager {

typedef std::vector<unsigned char> BinaryData;

enum MessageType {
  kUnknownType = -1,
  kRequest = 0,
  kResponse = 1,
  kNotification = 2,
  kErrorResponse = 3
};

enum ProtocolVersion {
  kUnknownProtocol = -1,
  kHMI = 0,
  kV1 = 1,
  kV2 = 2
};

class Message {
 public:
  Message();
  Message(const Message& message);
  Message& operator=(const Message& message);
  bool operator==(const Message& message);
  ~Message();

  //! --------------------------------------------------------------------------
  int function_id() const;
  int correlation_id() const;
  int connection_key() const;

  MessageType type() const;
  ProtocolVersion protocol_version() const;

  const std::string& json_message() const;
  const BinaryData* binary_data() const;
  bool has_binary_data() const;

  //! --------------------------------------------------------------------------
  void set_function_id(int id);
  void set_correlation_id(int id);
  void set_connection_key(int key);
  void set_message_type(MessageType type);
  void set_binary_data(BinaryData* data);
  void set_json_message(const std::string& json_message);
  void set_protocol_version(ProtocolVersion version);

 private:
  int function_id_;  // @remark protocol V2.
  int correlation_id_;  // @remark protocol V2.
  MessageType type_;  // @remark protocol V2.

  int connection_key_;
  ProtocolVersion version_;
  std::string json_message_;

  // TODO(akandul): replace with shared_ptr
  BinaryData* binary_data_;
};
}  // namespace application_manager

#endif  // SRC_COMPONENTS_APPLICATION_MANAGER_INCLUDE_APPLICATION_MANAGER_MESSAGE_H_
