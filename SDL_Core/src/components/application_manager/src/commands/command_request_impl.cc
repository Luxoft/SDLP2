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
#include "application_manager/commands/command_request_impl.h"
#include "application_manager/application_manager_impl.h"
#include "application_manager/message_chaining.h"
#include "application_manager/message_helper.h"
#include "smart_objects/smart_object.h"
#include "config_profile/profile.h"

namespace application_manager {

namespace commands {

CommandRequestImpl::CommandRequestImpl(const MessageSharedPtr& message)
 : CommandImpl(message),
   msg_chaining_(NULL),
   default_timeout_(profile::Profile::instance()->default_timeout()) {

}

CommandRequestImpl::~CommandRequestImpl() {
}

bool CommandRequestImpl::Init() {
  return true;
}

bool CommandRequestImpl::CleanUp() {
  return true;
}

void CommandRequestImpl::Run() {
}

void CommandRequestImpl::onTimeOut() const {
  LOG4CXX_INFO(logger_, "CommandRequestImpl::onTimeOut");

  smart_objects::SmartObject* response =
    MessageHelper::CreateNegativeResponse(connection_key(), function_id(),
    correlation_id(), mobile_api::Result::TIMED_OUT);

  ApplicationManagerImpl::instance()->ManageMobileCommand(response);
}

void CommandRequestImpl::on_event(const event_engine::Event& event) {
}

void CommandRequestImpl::SendResponse(
    const bool success, const mobile_apis::Result::eType& result_code,
    const char* info, const NsSmart::SmartObject* response_params) const {

  NsSmartDeviceLink::NsSmartObjects::SmartObject* result =
      new NsSmartDeviceLink::NsSmartObjects::SmartObject;
  if (!result) {
    LOG4CXX_ERROR(logger_, "Memory allocation failed.");
    return;
  }
  NsSmartDeviceLink::NsSmartObjects::SmartObject& response = *result;

  response[strings::params][strings::message_type] = MessageType::kResponse;
  response[strings::params][strings::correlation_id] = correlation_id();
  response[strings::params][strings::protocol_type] =
      CommandImpl::mobile_protocol_type_;
  response[strings::params][strings::protocol_version] =
      CommandImpl::protocol_version_;
  response[strings::params][strings::connection_key] = connection_key();
  response[strings::params][strings::function_id] = function_id();

  if (response_params) {
    response[strings::msg_params] = *response_params;
  }

  if (info) {
    response[strings::msg_params][strings::info] = std::string(info);
  }

  response[strings::msg_params][strings::success] = success;
  response[strings::msg_params][strings::result_code] = result_code;

  ApplicationManagerImpl::instance()->ManageMobileCommand(result);
}

void CommandRequestImpl::SendHMIRequest(
    const hmi_apis::FunctionID::eType& function_id,
    const NsSmart::SmartObject* msg_params, bool use_events) {

  NsSmartDeviceLink::NsSmartObjects::SmartObject* result =
      new NsSmartDeviceLink::NsSmartObjects::SmartObject;
  if (!result) {
    LOG4CXX_ERROR(logger_, "Memory allocation failed.");
    return;
  }

  const unsigned int hmi_correlation_id =
       ApplicationManagerImpl::instance()->GetNextHMICorrelationID();
  if (use_events) {
    subscribe_on_event(function_id, hmi_correlation_id);
  }

  NsSmartDeviceLink::NsSmartObjects::SmartObject& request = *result;
  request[strings::params][strings::message_type] = MessageType::kRequest;
  request[strings::params][strings::function_id] = function_id;
  request[strings::params][strings::correlation_id] = hmi_correlation_id;
  request[strings::params][strings::protocol_version] =
      CommandImpl::protocol_version_;
  request[strings::params][strings::protocol_type] =
      CommandImpl::hmi_protocol_type_;

  if (msg_params) {
    request[strings::msg_params] = *msg_params;
  }

  if (!ApplicationManagerImpl::instance()->ManageHMICommand(result)) {
    LOG4CXX_ERROR(logger_, "Unable to send request");
    SendResponse(false, mobile_apis::Result::OUT_OF_MEMORY);
  }
}

void CommandRequestImpl::CreateHMIRequest(
    const hmi_apis::FunctionID::eType& function_id,
    const NsSmart::SmartObject& msg_params, bool require_chaining,
    unsigned int chaining_counter) {

  NsSmartDeviceLink::NsSmartObjects::SmartObject* result =
      new NsSmartDeviceLink::NsSmartObjects::SmartObject;
  if (!result) {
    LOG4CXX_ERROR(logger_, "Memory allocation failed.");
    return;
  }

  const unsigned int correlation_id =
      (*message_)[strings::params][strings::correlation_id].asUInt();
  const unsigned int connection_key =
      (*message_)[strings::params][strings::connection_key].asUInt();

  // get hmi correlation id for chaining further request from this object
  const unsigned int hmi_correlation_id_ = ApplicationManagerImpl::instance()
      ->GetNextHMICorrelationID();

  NsSmartDeviceLink::NsSmartObjects::SmartObject& request = *result;
  request[strings::params][strings::message_type] = MessageType::kRequest;
  request[strings::params][strings::function_id] = function_id;
  request[strings::params][strings::correlation_id] = hmi_correlation_id_;
  request[strings::params][strings::protocol_version] =
      CommandImpl::protocol_version_;
  request[strings::params][strings::protocol_type] =
      CommandImpl::hmi_protocol_type_;

  request[strings::msg_params] = msg_params;

  if (require_chaining) {
    msg_chaining_ = ApplicationManagerImpl::instance()->AddMessageChain(
        connection_key, correlation_id, hmi_correlation_id_, msg_chaining_,
        &(*message_));

    if (!msg_chaining_) {
      LOG4CXX_ERROR(logger_, "Unable add request to MessageChain");
      SendResponse(false, mobile_apis::Result::OUT_OF_MEMORY);
      return;
    }

    if (0 < chaining_counter) {
      msg_chaining_->set_counter(chaining_counter);
    }
  }

  if (!ApplicationManagerImpl::instance()->ManageHMICommand(result)) {
    LOG4CXX_ERROR(logger_, "Unable to send request");
    SendResponse(false, mobile_apis::Result::OUT_OF_MEMORY);
  }
}

//TODO(VS): Should be removed after we will shift to Event engine usage completely
void CommandRequestImpl::CreateHMINotification(
    const hmi_apis::FunctionID::eType& function_id,
    const NsSmart::SmartObject& msg_params) const {

  NsSmartDeviceLink::NsSmartObjects::SmartObject* result =
      new NsSmartDeviceLink::NsSmartObjects::SmartObject;
  if (!result) {
    LOG4CXX_ERROR(logger_, "Memory allocation failed.");
    return;
  }
  NsSmartDeviceLink::NsSmartObjects::SmartObject& notify = *result;

  notify[strings::params][strings::message_type] = MessageType::kNotification;
  notify[strings::params][strings::function_id] = function_id;
  notify[strings::params][strings::protocol_version] =
      CommandImpl::protocol_version_;
  notify[strings::params][strings::protocol_type] =
      CommandImpl::hmi_protocol_type_;
  notify[strings::msg_params] = msg_params;

  if (!ApplicationManagerImpl::instance()->ManageHMICommand(result)) {
    LOG4CXX_ERROR(logger_, "Unable to send HMI notification");
  }
}

}  // namespace commands

}  // namespace application_manager
