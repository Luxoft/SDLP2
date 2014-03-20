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

#include <string>
#include "application_manager/commands/mobile/update_turn_list_request.h"
#include "application_manager/application_manager_impl.h"
#include "application_manager/application_impl.h"
#include "application_manager/message_helper.h"
#include "interfaces/MOBILE_API.h"
#include "interfaces/HMI_API.h"

namespace application_manager {

namespace commands {

UpdateTurnListRequest::UpdateTurnListRequest(const MessageSharedPtr& message)
    : CommandRequestImpl(message) {
}

UpdateTurnListRequest::~UpdateTurnListRequest() {
}

void UpdateTurnListRequest::Run() {
  LOG4CXX_INFO(logger_, "UpdateTurnListRequest::Run");

  Application* app = ApplicationManagerImpl::instance()->application(
      (*message_)[strings::params][strings::connection_key]);

  if (NULL == app) {
    SendResponse(false, mobile_apis::Result::APPLICATION_NOT_REGISTERED);
    LOG4CXX_ERROR(logger_, "Application is not registered");
    return;
  }

  mobile_apis::Result::eType processing_result =
      MessageHelper::ProcessSoftButtons((*message_)[strings::msg_params], app);

  if (mobile_apis::Result::SUCCESS != processing_result) {
    LOG4CXX_ERROR(logger_, "Wrong soft buttons parameters!");
    SendResponse(false, processing_result);
    return;
  }

  mobile_apis::Result::eType verification_result =
      MessageHelper::VerifyImageFiles((*message_)[strings::msg_params], app);

  if (mobile_apis::Result::SUCCESS != verification_result) {
    LOG4CXX_ERROR_EXT(
        logger_,
        "MessageHelper::VerifyImageFiles return " << verification_result);
    SendResponse(false, verification_result);
    return;
  }

  smart_objects::SmartObject msg_params = smart_objects::SmartObject(
      smart_objects::SmartType_Map);
  msg_params = (*message_)[strings::msg_params];
  for (int i = 0; i < msg_params[strings::turn_list].length(); ++i) {
    if (msg_params[strings::turn_list][i].keyExists(hmi_request::navi_text)) {
      std::string navigation_text =
          msg_params[strings::turn_list][i][hmi_request::navi_text].asString();
      msg_params[strings::turn_list][i].erase(hmi_request::navi_text);
      msg_params[strings::turn_list]
                 [i][hmi_request::navi_text][hmi_request::field_name] =
          hmi_apis::Common_TextFieldName::turnText;
      msg_params[strings::turn_list]
                 [i][hmi_request::navi_text][hmi_request::field_text] =
          navigation_text;
    }
  }

  msg_params[strings::app_id] = app->app_id();


  CreateHMIRequest(hmi_apis::FunctionID::Navigation_UpdateTurnList, msg_params,
                   true);
}

}  // namespace commands

}  // namespace application_manager
