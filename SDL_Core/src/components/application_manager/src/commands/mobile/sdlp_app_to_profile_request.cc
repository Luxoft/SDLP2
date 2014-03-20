/*
*
* iviLINK SDK
* http://www.ivilink.net
* Cross Platform Application Communication Stack for In-Vehicle Applications
*
* Copyright (C) 2013, Luxoft Professional Corp., member of IBS group
*
* This library is free software; you can redistribute it and/or
* modify it under the terms of the GNU Lesser General Public
* License as published by the Free Software Foundation; version 2.1.
*
* This library is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
* Lesser General Public License for more details.
*
* You should have received a copy of the GNU Lesser General Public
* License along with this library; if not, write to the Free Software
* Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
*
*
*/
/**
 * @author Elena Bratanova <ebratanova@luxoft.com>
 */

#include "application_manager/commands/mobile/sdlp_app_to_profile_request.h"
#include "application_manager_profile_manager_interface/profile_manager_holder.h"

namespace application_manager
{

namespace commands
{

AppToProfileRequest::AppToProfileRequest(const MessageSharedPtr& message)
    : CommandRequestImpl(message)
{
}

AppToProfileRequest::~AppToProfileRequest()
{
}

void AppToProfileRequest::Run()
{
    LOG4CXX_INFO(logger_, "AppToProfileRequest::Run");
    assert ((*message_)[strings::params].keyExists(strings::binary_data));
    const std::vector<unsigned char> fileData = (*message_)[strings::params][strings::binary_data].asBinary();
    const char * castFile = reinterpret_cast<const char*>((&fileData[0]));
    std::string profileName = (*message_)[strings::msg_params][strings::profile_name].asString();
    int appID = (*message_)[strings::params][strings::connection_key];
    profile_manager::IProfileManager * mgr = profile_manager::ProfileManagerHolder::getProfileManagerInstance();
    if (!mgr)
    {
        SendResponse(false, mobile_apis::Result::REJECTED, "No access to profile manager!");
        return;
    }
    ProfileManagerResult result = mgr->sendAppToProfileMessage(appID, profileName, castFile, fileData.size());
    SendResponse(result.isSuccess, result.retcode, result.description.c_str());
}


}  // namespace commands

}  // namespace application_manager
