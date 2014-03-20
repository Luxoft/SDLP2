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

#ifndef __RECEIVER_ZMQ_SOCKET_H__
#define __RECEIVER_ZMQ_SOCKET_H__

#include "Logger.hpp"
#include "zmq_socket/socket_connection_type.h"
#include "zmq_socket/zmq_socket.h"
#include "zmq_socket/zmq_socket_common.h"

namespace NsUtils
{

class IReceiverSocketListener
{
public:
    virtual ~IReceiverSocketListener() {}
    /**
     * Invoked when the socket receives a correct message.
     * A response is sent automatically.
     * Data payload of the message (if there is any)
     * is deallocated after this callback, so you should copy it
     * if you intend to keep it for later use.
     */
    virtual void handleMessage(tSizedMessage message) = 0;
};

/**
 * \brief has internal thread to listen to the socket;
 * preserves the recv-send order of ZMQ_REP sockets
 */
class ReceiverZmqSocket : public ZmqSocket
{
public:
    /**
     * Create the socket and start its internal listening thread.
     */
    ReceiverZmqSocket(void * zmqContext, std::string const& address, SocketConnectionType type,
                      IReceiverSocketListener * listener);

    /**
     * Delete the socket object and stop its internal thread.
     */
    ~ReceiverZmqSocket();

private:
    virtual void threadFunc();

private:
    IReceiverSocketListener * mListener;
protected:
    static Logger mLogger;

};

}

#endif //__RECEIVER_ZMQ_SOCKET_H__
