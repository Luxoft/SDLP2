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

#include <sys/stat.h>

#include "zmq_socket/zmq_socket.h"
#include "zmq_socket/zmq_socket_common.h"

using namespace NsUtils;

Logger ZmqSocket::mLogger = Logger::getInstance("Utils.ZmqSocket.ZmqSocket");

ZmqSocket::ZmqSocket(void * zmqContext, const std::string& address, SocketConnectionType type,
                     int zmqSocketType, int pollTimeout)
    : mConnectionType(type)
    , mSocketAddress(address)
    , mContext(zmqContext)
    , mZmqSocketType(zmqSocketType)
    , mPollTimeout(pollTimeout)
    , mStop(false)
{
    if (mkdir(ZMQ_SOCKET_DIR, S_IRWXU|S_IRGRP|S_IXGRP) || EEXIST == errno)
    {
        LOG4CPLUS_INFO(mLogger, "Folder " << ZMQ_SOCKET_DIR << " exists");
    }
    else
    {
        LOG4CPLUS_ERROR(mLogger, "Folder " << ZMQ_SOCKET_DIR << " could not be created");
    }
}

ZmqSocket::~ZmqSocket()
{
}

void ZmqSocket::startThread()
{
    LOG4CPLUS_TRACE_METHOD(mLogger, __PRETTY_FUNCTION__);
    pthread_create((pthread_t*) &mThread, 0, threadFuncRunner, (void*)this);
}

void * ZmqSocket::threadFuncRunner(void * self)
{
    ((ZmqSocket*)self)->threadFunc();
    return NULL;
}

void ZmqSocket::joinThread()
{
    LOG4CPLUS_TRACE_METHOD(mLogger, __PRETTY_FUNCTION__);
    mStop = true;
    pthread_join(mThread, 0);
}

bool ZmqSocket::sendMessage(const char * data, const int dataSize, void * socket)
{
    LOG4CPLUS_TRACE_METHOD(mLogger, __PRETTY_FUNCTION__);
    if (!socket || !data || dataSize == 0)
    {
        return false;
    }
    if (poll(socket, ZMQ_POLLOUT))
    {
        int rc = zmq_send(socket, data, dataSize, ZMQ_NOBLOCK);
        LOG4CPLUS_INFO(mLogger, "non-blocking send result: " << rc);
        return rc == dataSize;
    }
    return false;
}

bool ZmqSocket::receiveMessage(zmq_msg_t &message, void * socket)
{
    LOG4CPLUS_TRACE_METHOD(mLogger, __PRETTY_FUNCTION__);
    if (!socket)
    {
        return false;
    }
    if (poll(socket, ZMQ_POLLIN))
    {
        int rc = zmq_msg_recv(&message, socket, ZMQ_NOBLOCK);
        LOG4CPLUS_INFO(mLogger, "non-blocking recv result: " << rc);
        return rc != -1;
    }
    return false;
}

void * ZmqSocket::recreateSocket(void * oldSocket)
{
    LOG4CPLUS_TRACE_METHOD(mLogger, __PRETTY_FUNCTION__);
    if (oldSocket != NULL)
    {
        zmq_close(oldSocket);
    }
    if (mStop)
    {
        return NULL;
    }
    void * newSocket = zmq_socket(mContext, mZmqSocketType);
    int lingerTimeo = 0;
    zmq_setsockopt(newSocket, ZMQ_LINGER, &lingerTimeo, sizeof(int));
    if (newSocket)
    {
        while (!mStop)
        {
            int res = -1;
            if (mConnectionType == CONNECT)
            {
                res = zmq_connect(newSocket, mSocketAddress.c_str());
            }
            else if (mConnectionType == BIND)
            {
                res = zmq_bind(newSocket, mSocketAddress.c_str());
            }

            if (res == 0)
            {
                LOG4CPLUS_INFO(mLogger, "Successfully created socket to " + mSocketAddress);
                break;
            }
            usleep(2000);
        }
    }
    else
    {
        // context-related errors
        if (errno == EFAULT || errno == ETERM)
        {
            LOG4CPLUS_ERROR(mLogger, "Invalid context provided");
            assert (false);
        }
    }
    return newSocket;
}


bool ZmqSocket::poll(void * socket, int eventType)
{
    LOG4CPLUS_TRACE_METHOD(mLogger, __PRETTY_FUNCTION__);
    zmq_pollitem_t pollItem[1];
    pollItem[0].socket = socket;
    pollItem[0].events = eventType;

    while(true)
    {
        int rc = zmq_poll(pollItem, 1, mPollTimeout);
        assert (rc >= 0); // it may be < 0 only if context has been broken
        if (pollItem[0].revents == 0)
        {
            // nothing is happening with the socket
        }
        if (mStop)
        {
            return false;
        }
        if (pollItem[0].revents == eventType)
        {
            LOG4CPLUS_INFO(mLogger, "Poll success");
            return true;
        }
    }
}
