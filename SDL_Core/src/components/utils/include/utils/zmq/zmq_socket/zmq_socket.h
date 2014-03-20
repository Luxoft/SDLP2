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


#ifndef __ZMQSOCKETH__
#define __ZMQSOCKETH__

#include <zmq.h>
#include <string>

#include "Logger.hpp"
#include "zmq_socket/socket_connection_type.h"

namespace NsUtils
{
/**
 * \brief Abstract base class for Sender and Receiver sockets.
 */
class ZmqSocket
{
public:
    ZmqSocket(void * zmqContext,
              const std::string& address,
              SocketConnectionType type,
              int zmqSocketType,
              int pollTimeout = DEFAULT_POLL_TIMEOUT_MS);
    virtual ~ZmqSocket();

protected:

    /**
     * Start a thread that runs threadFunc.
     */
    void startThread();

    /**
     * Set stop flag to true and waits for internal thread with
     * threadFunc to join.
     */
    void joinThread();

    /**
     * Should check mStop periodically.
     */
    virtual void threadFunc() = 0;
    /**
     * Send data to socket.
     * @return true in case of success; false otherwise
     */
    bool sendMessage(const char * data, const int dataSize, void * socket);
    /**
     * Receive data from socket. Data is written into message.
     * @return true in case of success; false otherwise
     */
    bool receiveMessage(zmq_msg_t &message, void * socket);

    /**
     * Close old socket and create a new one of type mZmqSocketType,
     * with address mSocketAddress (address is either bound or connected to
     * depending on mConnectionType).
     * @param oldSocket socket to close or NULL
     * @return newly-created socket or NULL (if mContext is invalid
     * or if could not bind/connect to address).
     */
    void * recreateSocket(void * oldSocket);

private:

    static void * threadFuncRunner(void * self);

    /**
     * Polls socket for eventType. Polling is done in cycle (time between
     * iterations equals to mPollTimeout) until either mStop becomes true,
     * or desired event happens.
     * @return true if eventType happened; false otherwise
     */
    bool poll(void * socket, int eventType);

protected:
    volatile bool mStop;

private:
    pthread_t mThread;
    SocketConnectionType mConnectionType;
    std::string mSocketAddress;
    void * mContext;
    int mZmqSocketType;
    int mPollTimeout;

    static Logger mLogger;

};
}

#endif //__ZMQ_SOCKET_H__
