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


#ifndef __PM_RECEIVER_ZMQ_SOCKET_H__
#define __PM_RECEIVER_ZMQ_SOCKET_H__

#include "zmq_socket/receiver_zmq_socket.h"
#include "profile_manager/profile_manager_common.h"

namespace NsProfileManager
{
class IPMSocketListener
{
public:
    virtual ~IPMSocketListener() {}
    virtual void handlePmMessage(ZmqMessageHeader header, const char * data) = 0;
};

class PMReceiverZmqSocket : public NsUtils::ReceiverZmqSocket, public NsUtils::IReceiverSocketListener
{
public:
    PMReceiverZmqSocket(void * zmqContext, std::string const& address, NsUtils::SocketConnectionType type,
                        IPMSocketListener * listener);
    virtual ~PMReceiverZmqSocket() {};

protected:
    virtual void handleMessage(NsUtils::tSizedMessage msg);

private:
    IPMSocketListener * mPmListener;
};
}

#endif// __PM_RECEIVER_ZMQ_SOCKET_H__