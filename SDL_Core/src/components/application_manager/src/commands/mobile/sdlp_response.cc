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

#include "application_manager/commands/mobile/sdlp_response.h"
#include "application_manager/application_manager_impl.h"

namespace application_manager
{

namespace commands
{

SDLPResponse::SDLPResponse(const MessageSharedPtr& message)
    : CommandResponseImpl(message)
{
}

SDLPResponse::~SDLPResponse()
{
}

void SDLPResponse::Run()
{
    LOG4CXX_INFO(logger_, "SDLPResponse::Run");
    ApplicationManagerImpl::instance()->SendMessageToMobile(message_);
}

}  // namespace commands

}  // namespace application_manager
