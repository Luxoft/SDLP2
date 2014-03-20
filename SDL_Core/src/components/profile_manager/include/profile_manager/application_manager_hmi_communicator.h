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

#ifndef __APPLICATION_MANAGER_HMI_COMMUNICATOR_H__
#define __APPLICATION_MANAGER_HMI_COMMUNICATOR_H__

#include "profile_manager/profile_instance_manager.h"
#include "zmq_socket/sender_zmq_socket.h"
#include "zmq_socket/receiver_zmq_socket.h"

namespace NsProfileManager
{
/**
 * Sends messages from profiles to Apps (via AppManHmi),
 * receives messages from Apps and passes them to profiles via ProfileInstanceManager
 */
class ApplicationManagerHmiCommunicator : public NsUtils::IReceiverSocketListener
{
public:
    ApplicationManagerHmiCommunicator(ProfileInstanceManager * pim);
    virtual ~ApplicationManagerHmiCommunicator();
    void sendMessageToAppManager(const char * data, const unsigned int dataSize);
private:
    virtual void handleMessage(std::pair<uint32_t, const char*> message);

private:
    ProfileInstanceManager  * mPIM;
    NsUtils::SenderZmqSocket * mSenderSocket;
    NsUtils::ReceiverZmqSocket * mReceiverSocket;
    void * mContext;
};
}

#endif //__APPLICATION_MANAGER_HMI_COMMUNICATOR_H__
