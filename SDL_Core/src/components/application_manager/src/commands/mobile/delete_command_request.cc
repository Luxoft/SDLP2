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

#include "application_manager/commands/mobile/delete_command_request.h"
#include "application_manager/application_manager_impl.h"
#include "application_manager/application_impl.h"
#include "interfaces/MOBILE_API.h"
#include "interfaces/HMI_API.h"

namespace application_manager {

namespace commands {

DeleteCommandRequest::DeleteCommandRequest(const MessageSharedPtr& message)
    : CommandRequestImpl(message) {
}

DeleteCommandRequest::~DeleteCommandRequest() {
}

void DeleteCommandRequest::Run() {
  LOG4CXX_INFO(logger_, "DeleteCommandRequest::Run");

  Application* application = ApplicationManagerImpl::instance()->application(
      (*message_)[strings::params][strings::connection_key]);

  if (!application) {
    SendResponse(false, mobile_apis::Result::APPLICATION_NOT_REGISTERED);
    LOG4CXX_ERROR(logger_, "Application is not registered");
    return;
  }

  smart_objects::SmartObject* command = application->FindCommand(
      (*message_)[strings::msg_params][strings::cmd_id].asInt());

  if (!command) {
    SendResponse(false, mobile_apis::Result::INVALID_ID);
    LOG4CXX_ERROR(logger_, "Invalid ID");
    return;
  }

  smart_objects::SmartObject msg_params = smart_objects::SmartObject(
      smart_objects::SmartType_Map);

  msg_params[strings::cmd_id] =
      (*message_)[strings::msg_params][strings::cmd_id];
  msg_params[strings::app_id] = application->app_id();

  // we should specify amount of required responses in the 1st request
  unsigned int chaining_counter = 0;
  if ((*command).keyExists(strings::menu_params)) {
    ++chaining_counter;
  }

  if ((*command).keyExists(strings::vr_commands)) {
    ++chaining_counter;
  }

  if ((*command).keyExists(strings::menu_params)) {
    CreateHMIRequest(hmi_apis::FunctionID::UI_DeleteCommand, msg_params, true,
                     chaining_counter);
  }
  // check vr params
  if ((*command).keyExists(strings::vr_commands)) {
    // check if only vr
    if (1 == chaining_counter) {
      CreateHMIRequest(hmi_apis::FunctionID::VR_DeleteCommand, msg_params, true,
                       chaining_counter);
    } else {
      CreateHMIRequest(hmi_apis::FunctionID::VR_DeleteCommand, msg_params,
                       true);
    }
  }
}

}  // namespace commands

}  // namespace application_manager
