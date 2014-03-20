/**
 *
 * SDLP SDK
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

#include <string.h>
#include "profile_manager/ipc/pm_receiver_zmq_socket.h"

using namespace NsProfileManager;

PMReceiverZmqSocket::PMReceiverZmqSocket(void * zmqContext, std::string const& address, NsUtils::SocketConnectionType type,
        IPMSocketListener * listener)
    : NsUtils::ReceiverZmqSocket(zmqContext, address, type, this)
    , mPmListener(listener)
{
}

void PMReceiverZmqSocket::handleMessage(NsUtils::tSizedMessage msg)
{
    uint32_t dataSize = msg.first;
    const char * data = msg.second;
    if (dataSize < sizeof(ZmqMessageHeader))
    {
        LOG4CPLUS_ERROR(mLogger, "Message is too small");
        return;
    }
    ZmqMessageHeader * messageHeader = reinterpret_cast<ZmqMessageHeader*>((char*)data);
    if (dataSize < sizeof(ZmqMessageHeader) + messageHeader->dataSize)
    {
        LOG4CPLUS_ERROR(mLogger, "Message is inconsistent");
        return;
    }
    const char * messageData = data + sizeof(ZmqMessageHeader);
    mPmListener->handlePmMessage(*messageHeader, messageData);
}
