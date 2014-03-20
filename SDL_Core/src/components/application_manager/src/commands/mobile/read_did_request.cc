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

#include "application_manager/commands/mobile/read_did_request.h"
#include "application_manager/application_manager_impl.h"
#include "application_manager/application_impl.h"
#include "interfaces/MOBILE_API.h"
#include "interfaces/HMI_API.h"

namespace application_manager {

namespace commands {

ReadDIDRequest::ReadDIDRequest(const MessageSharedPtr& message)
    : CommandRequestImpl(message) {
}

ReadDIDRequest::~ReadDIDRequest() {
}

void ReadDIDRequest::Run() {
  LOG4CXX_INFO(logger_, "ReadDIDRequest::Run");

  unsigned int app_id = (*message_)[strings::params][strings::connection_key]
      .asUInt();
  Application* app = ApplicationManagerImpl::instance()->application(app_id);

  if (!app) {
    LOG4CXX_ERROR_EXT(logger_, "An application is not registered.");
    SendResponse(false, mobile_apis::Result::APPLICATION_NOT_REGISTERED);
    return;
  }

  if (mobile_api::HMILevel::HMI_NONE == app->hmi_level()) {
    SendResponse(false, mobile_apis::Result::REJECTED);
    LOG4CXX_ERROR(logger_, "Rejected");
    return;
  }

  smart_objects::SmartObject msg_params = smart_objects::SmartObject(
      smart_objects::SmartType_Map);
  msg_params[strings::app_id] = app->app_id();
  msg_params[strings::ecu_name] =
      (*message_)[strings::msg_params][strings::ecu_name];
  msg_params[strings::did_location] =
      (*message_)[strings::msg_params][strings::did_location];

  CreateHMIRequest(hmi_apis::FunctionID::VehicleInfo_ReadDID, msg_params, true,
                   1);
}

}  // namespace commands

}  // namespace application_manager
