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
#include "profile_manager/ipc/pm_sender_zmq_socket.h"

using namespace NsProfileManager;

PMSenderZmqSocket::PMSenderZmqSocket(void * zmqContext, std::string const& address, NsUtils::SocketConnectionType type)
    : NsUtils::SenderZmqSocket(zmqContext, address, type)
{
}

void PMSenderZmqSocket::pushMessageToQueue(const int appId, const MessageTypes type,
        int state, int protocolVersion, const int dataSize, const char * data)
{
    ZmqMessageHeader header;
    header.appId = appId;
    header.messageType = type;
    header.state = state;
    header.protocolVersion = protocolVersion;
    header.dataSize = dataSize;

    char buffer[sizeof(ZmqMessageHeader) + dataSize];
    memcpy(buffer, &header, sizeof(ZmqMessageHeader));

    if (dataSize > 0)
    {
        memcpy(buffer + sizeof(ZmqMessageHeader), data, dataSize);
    }
    NsUtils::SenderZmqSocket::pushMessageToQueue(sizeof(buffer), buffer);
}
