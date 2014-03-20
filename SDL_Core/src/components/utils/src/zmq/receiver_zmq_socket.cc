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

#include "zmq_socket/receiver_zmq_socket.h"

using namespace NsUtils;

Logger ReceiverZmqSocket::mLogger = Logger::getInstance("Utils.ZmqSocket.ReceiverZmqSocket");

ReceiverZmqSocket::ReceiverZmqSocket(void * zmqContext, std::string const& address, SocketConnectionType type,
                                     IReceiverSocketListener * listener)
    : ZmqSocket(zmqContext, address, type, ZMQ_REP)
    , mListener(listener)
{
    LOG4CPLUS_TRACE_METHOD(mLogger, __PRETTY_FUNCTION__);
    ZmqSocket::startThread();
}

ReceiverZmqSocket::~ReceiverZmqSocket()
{
    LOG4CPLUS_TRACE_METHOD(mLogger, __PRETTY_FUNCTION__);
    ZmqSocket::joinThread();
}

void ReceiverZmqSocket::threadFunc()
{
    void * receiveSocket = ZmqSocket::recreateSocket(NULL);
    if (!receiveSocket)
    {
        LOG4CPLUS_ERROR(mLogger, "Could not create socket!");
        return;
    }

    while (!mStop)
    {
        zmq_msg_t msg;
        if (zmq_msg_init(&msg) != 0)
        {
            LOG4CPLUS_ERROR(mLogger, "Could not init message");
            continue;
        }
        if (ZmqSocket::receiveMessage(msg, receiveSocket))
        {
            if (!ZmqSocket::sendMessage(MSG_REPLY_OK, sizeof(MSG_REPLY_OK), receiveSocket))
            {
                LOG4CPLUS_ERROR(mLogger, "Send reply failed");
                if (!mStop)
                {
                    receiveSocket = ZmqSocket::recreateSocket(receiveSocket);
                }
                zmq_msg_close(&msg);
                continue;
            }
            char *data = (char *)zmq_msg_data(&msg);
            int dataSize = zmq_msg_size(&msg);

            tSizedMessage msg = std::make_pair(dataSize, data);

            if (mListener)
            {
                mListener->handleMessage(msg);
            }
            else
            {
                LOG4CPLUS_WARN(mLogger, "No listener for socket");
            }
        }
        else
        {
            LOG4CPLUS_ERROR(mLogger, "Receive failed");
            if (!mStop)
            {
                receiveSocket = ZmqSocket::recreateSocket(receiveSocket);
            }
        }
        zmq_msg_close(&msg);
    }
    if (receiveSocket)
    {
        zmq_close(receiveSocket);
    }
}
