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


#ifndef __ZMQ_UTILS_SENDER_ZMQ_SOCKET_H__
#define __ZMQ_UTILS_SENDER_ZMQ_SOCKET_H__

#include <queue>
#include <string>
#include <pthread.h>
#include <semaphore.h>

#include "Logger.hpp"
#include "zmq_socket/socket_connection_type.h"
#include "zmq_socket/zmq_socket.h"
#include "zmq_socket/zmq_socket_common.h"

namespace NsUtils
{

/**
 * \brief has internal queue; preserves the send-recv order of ZMQ_REQ sockets
 * (sends an enqueued message and waits for a reply on a background thread)
 */
class SenderZmqSocket : public ZmqSocket
{
public:
    /**
     * Create a socket and start its internal dequeueing thread.
     */
    SenderZmqSocket(void * zmqContext, std::string const& address, SocketConnectionType type);
    ~SenderZmqSocket();

    /**
     * A ZmqMessage is formed and enqueued for sending.
     */
    void pushMessageToQueue(const int dataSize, const char * data);

private:
    virtual void threadFunc();
private:
    std::queue<tSizedMessage> mMessageQueue;
    pthread_mutex_t mMessageQueueMutex;
    sem_t mMessageQueueSema;

    static Logger mLogger;
};

}

#endif //__SENDER_ZMQ_SOCKET_H__
