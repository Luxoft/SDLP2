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


#include <zmq.h>
#include <string.h>
#include <errno.h>
#include <pthread.h>

#include "zmq_socket/sender_zmq_socket.h"

using namespace NsUtils;

Logger SenderZmqSocket::mLogger = Logger::getInstance("Utils.ZmqSocket.SenderZmqSocket");

SenderZmqSocket::SenderZmqSocket(void * zmqContext, std::string const& address, SocketConnectionType type)
    : ZmqSocket(zmqContext, address, type, ZMQ_REQ)
{
    LOG4CPLUS_TRACE_METHOD(mLogger, __PRETTY_FUNCTION__);
    pthread_mutex_init(&mMessageQueueMutex, NULL);
    sem_init(&mMessageQueueSema, 0, 0);
    ZmqSocket::startThread();
}

SenderZmqSocket::~SenderZmqSocket()
{
    LOG4CPLUS_TRACE_METHOD(mLogger, __PRETTY_FUNCTION__);
    mStop = true;
    sem_post(&mMessageQueueSema);
    ZmqSocket::joinThread();
    pthread_mutex_lock(&mMessageQueueMutex);
    while (!mMessageQueue.empty())
    {
        LOG4CPLUS_INFO(mLogger, "~SenderZmqSocket cleanup queue");
        tSizedMessage msg = mMessageQueue.front();
        mMessageQueue.pop();
        if (msg.first != 0 && msg.second != NULL)
        {
            delete[] msg.second;
        }
    }
    pthread_mutex_unlock(&mMessageQueueMutex);
    sem_destroy(&mMessageQueueSema);
    pthread_mutex_destroy(&mMessageQueueMutex);
}

void SenderZmqSocket::pushMessageToQueue(const int dataSize, const char * data)
{
    LOG4CPLUS_TRACE_METHOD(mLogger, __PRETTY_FUNCTION__);
    if (dataSize != 0 && data != NULL)
    {
        pthread_mutex_lock(&mMessageQueueMutex);
        char * dataCopy = new char[dataSize];
        memcpy(dataCopy, data, dataSize);
        mMessageQueue.push(std::make_pair(dataSize, dataCopy));
        pthread_mutex_unlock(&mMessageQueueMutex);
        sem_post(&mMessageQueueSema);
    }
}

void SenderZmqSocket::threadFunc()
{
    LOG4CPLUS_TRACE_METHOD(mLogger, __PRETTY_FUNCTION__);
    void * sendSocket = recreateSocket(NULL);
    if (!sendSocket)
    {
        LOG4CPLUS_ERROR(mLogger, "Could not create a socket!");
        return;
    }

    while (!mStop)
    {
        sem_wait(&mMessageQueueSema);
        if (mStop)
        {
            LOG4CPLUS_INFO(mLogger, "Sema wait interrupted on stop request");
            break;
        }
        pthread_mutex_lock(&mMessageQueueMutex);
        if (mMessageQueue.empty())
        {
            LOG4CPLUS_INFO(mLogger, "mMessageQueue is empty");
            pthread_mutex_unlock(&mMessageQueueMutex);
            continue;
        }

        tSizedMessage message = mMessageQueue.front();
        mMessageQueue.pop();
        pthread_mutex_unlock(&mMessageQueueMutex);

        if (ZmqSocket::sendMessage(message.second, message.first, sendSocket))
        {
            zmq_msg_t reply;
            zmq_msg_init(&reply);
            if (!ZmqSocket::receiveMessage(reply, sendSocket))
            {
                LOG4CPLUS_ERROR(mLogger, "Receive reply error!");
                if (!mStop)
                {
                    sendSocket = ZmqSocket::recreateSocket(sendSocket);
                }
            }
            zmq_msg_close(&reply);
        }
        else
        {
            LOG4CPLUS_ERROR(mLogger, "Send request error!");
            if (!mStop)
            {
                sendSocket = ZmqSocket::recreateSocket(sendSocket);
            }
        }
    }
    if (sendSocket)
    {
        zmq_close(sendSocket);
    }
}
