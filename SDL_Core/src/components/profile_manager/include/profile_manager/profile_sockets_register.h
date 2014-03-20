/*
 *
 * iviLINK SDK
 * http://www.ivilink.net
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


#ifndef __PROFILE_SOCKETS_REGISTER_H__
#define __PROFILE_SOCKETS_REGISTER_H__

#include <pthread.h>
#include <zmq.h>
#include <string>

#include "Logger.hpp"

#include "profile_manager/profile_manager_common.h"
#include "profile_manager/ipc/pm_sender_zmq_socket.h"


namespace NsProfileManager
{

/**
 * \namespace NsProfileManager
 * \brief Creates and stores zeromq sockets for profiles.
 */
class ProfileSocketsRegister
{
    typedef std::map<std::string, PMSenderZmqSocket*> tSocketsMap;
public:
    ProfileSocketsRegister(void * zmqContext);
    ~ProfileSocketsRegister();

    std::string createSocketForProfile(std::string const& profileName);

    void destroySocketForProfile(std::string const& profileName);

    void sendMessageToProfile(std::string const& profileName,const int appId,
                              const MessageTypes, int state, int protocolVersion,
                              const int dataSize, const char * data);

private:
    void * mZmqContext;
    tSocketsMap mSockets;
    pthread_mutex_t mSocketsMutex;

    static Logger mLogger;
};

}

#endif //__PROFILE_SOCKETS_REGISTER_HPP__
