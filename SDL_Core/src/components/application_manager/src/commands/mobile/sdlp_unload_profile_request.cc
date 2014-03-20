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

#include "application_manager/commands/mobile/sdlp_unload_profile_request.h"
#include "application_manager_profile_manager_interface/profile_manager_holder.h"

namespace application_manager
{

namespace commands
{

UnloadProfileRequest::UnloadProfileRequest(const MessageSharedPtr& message)
    : CommandRequestImpl(message)
{
}

UnloadProfileRequest::~UnloadProfileRequest()
{
}

void UnloadProfileRequest::Run()
{
    std::string profileName = (*message_)[strings::msg_params][strings::profile_name].asString();
    int appID = (*message_)[strings::params][strings::connection_key];
    profile_manager::IProfileManager * mgr = profile_manager::ProfileManagerHolder::getProfileManagerInstance();
    if (!mgr)
    {
        SendResponse(false, mobile_apis::Result::REJECTED, "No access to profile manager!");
        return;
    }
    ProfileManagerResult result = mgr->unloadProfile(appID, profileName);
    SendResponse(result.isSuccess, result.retcode, result.description.c_str());
}


}  // namespace commands

}  // namespace application_manager
