/*

 Copyright (c) 2013, Ford Motor Company
 All rights reserved.

 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions are met:

 Redistributions of source code must retain the above copyright notice, this
 list of conditions and the following disclaimer.

 Redistributions in binary form must reproduce the above copyright notice,
 this list of conditions and the following
 disclaimer in the documentation and/or other materials provided with the
 distribution.

 Neither the name of the Ford Motor Company nor the names of its contributors
 may be used to endorse or promote products derived from this software
 without specific prior written permission.

 THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 POSSIBILITY OF SUCH DAMAGE.
 */

#include "application_manager/commands/mobile/read_did_response.h"
#include "application_manager/application_manager_impl.h"
#include "interfaces/HMI_API.h"

namespace application_manager {

namespace commands {

ReadDIDResponse::ReadDIDResponse(const MessageSharedPtr& message)
    : CommandResponseImpl(message) {
}

ReadDIDResponse::~ReadDIDResponse() {
}

void ReadDIDResponse::Run() {
  LOG4CXX_INFO(logger_, "ReadDIDResponse::Run");

  // check if response false
  if (true == (*message_)[strings::msg_params].keyExists(strings::success)) {
    if ((*message_)[strings::msg_params][strings::success].asBool() == false) {
      LOG4CXX_ERROR(logger_, "Success = false");
      SendResponse(false);
      return;
    }
  }

  /*if ((*message_)[strings::msg_params].keyExists(hmi_response::did_result)) {
   (*message_)[strings::msg_params][strings::data_result] =
   (*message_)[strings::msg_params][hmi_response::did_result]
   [hmi_response::result_code];

   (*message_)[strings::msg_params][strings::data] =
   (*message_)[strings::msg_params][hmi_response::did_result]
   [strings::data];

   (*message_)[strings::msg_params].erase(hmi_response::did_result);
   }*/

  if (!IsPendingResponseExist()) {
    const int code = (*message_)[strings::params][hmi_response::code].asInt();

    if (hmi_apis::Common_Result::SUCCESS == code) {
      SendResponse(true);
    } else {
      // TODO(DK): Some logic
      SendResponse(false);
    }
  }
}

}  // namespace commands

}  // namespace application_manager
