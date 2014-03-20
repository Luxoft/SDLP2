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

#include "application_manager/commands/mobile/encoded_sync_pdata_request.h"
#include "application_manager/application_manager_impl.h"
#include "application_manager/application_impl.h"
#include "interfaces/MOBILE_API.h"
#include "utils/file_system.h"
#include "encryption/Base64.h"

namespace application_manager {

namespace commands {

const std::string EncodedSyncPDataRequest::TEMPORARY_HARDCODED_FILENAME =
    "policy_sync_data.dat";
const std::string EncodedSyncPDataRequest::TEMPORARY_HARDCODED_FOLDERNAME =
    "policies/";

EncodedSyncPDataRequest::EncodedSyncPDataRequest(
    const MessageSharedPtr& message)
    : CommandRequestImpl(message) {
}

EncodedSyncPDataRequest::~EncodedSyncPDataRequest() {
}

void EncodedSyncPDataRequest::Run() {
  LOG4CXX_INFO(logger_, "EncodedSyncPDataRequest::Run");

  unsigned int app_id = (*message_)[strings::params][strings::connection_key]
      .asUInt();
  Application* app = ApplicationManagerImpl::instance()->application(app_id);

  if (NULL == app) {
    LOG4CXX_ERROR(logger_, "NULL pointer");
    SendResponse(false, mobile_apis::Result::APPLICATION_NOT_REGISTERED);
    return;
  }

  unsigned int free_space = file_system::AvailableSpaceApp(app->name());

  const std::string& sync_file_name = TEMPORARY_HARDCODED_FILENAME;

  std::string encoded_string_pdata;

  size_t data_array_size = (*message_)[strings::msg_params][strings::data]
      .length();

  for (size_t i = 0; i < data_array_size; ++i) {
    encoded_string_pdata += (*message_)[strings::msg_params][strings::data][i]
        .asString();
  }

  LOG4CXX_INFO(logger_, "encoded_string_pdata = " << encoded_string_pdata);

  const std::string string_pdata = base64_decode(encoded_string_pdata);

  LOG4CXX_INFO(logger_, "string_pdata = " << string_pdata);

  const std::vector<unsigned char> char_vector_pdata(string_pdata.begin(),
                                                     string_pdata.end());

  if (free_space > string_pdata.size()) {
    std::string relative_file_path = file_system::CreateDirectory(
        TEMPORARY_HARDCODED_FOLDERNAME);

    relative_file_path += sync_file_name;

    LOG4CXX_INFO(logger_, "relative_file_path = " << relative_file_path);

    if (file_system::Write(file_system::FullPath(relative_file_path),
                           char_vector_pdata)) {
      LOG4CXX_INFO(logger_, "Successfully write data to file");
      SendResponse(true, mobile_apis::Result::SUCCESS);
    } else {
      LOG4CXX_ERROR(logger_, "Failed write to file");
      SendResponse(false, mobile_apis::Result::GENERIC_ERROR);
    }
  } else {
    SendResponse(false, mobile_apis::Result::OUT_OF_MEMORY);
  }
}

}  // namespace commands

}  // namespace application_manager
