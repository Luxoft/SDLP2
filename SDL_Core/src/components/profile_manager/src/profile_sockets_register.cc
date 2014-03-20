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


#include <cstdlib>
#include "Logger.hpp"

#include "profile_manager/profile_sockets_register.h"

using namespace NsProfileManager;

Logger ProfileSocketsRegister::mLogger = Logger::getInstance("SDL.ProfileManager.ProfileSocketsRegister");

ProfileSocketsRegister::ProfileSocketsRegister(void * context)
    : mZmqContext(context)
{
    LOG4CPLUS_TRACE_METHOD(mLogger, __PRETTY_FUNCTION__);
    pthread_mutex_init(&mSocketsMutex, NULL);
}

ProfileSocketsRegister::~ProfileSocketsRegister()
{
    LOG4CPLUS_TRACE_METHOD(mLogger, __PRETTY_FUNCTION__);
    pthread_mutex_lock(&mSocketsMutex);
    tSocketsMap::iterator iter = mSockets.begin();
    while (iter != mSockets.end())
    {
        if (iter->second != NULL)
        {
            delete iter->second;
        }
        iter ++;
    }
    mSockets.clear();
    pthread_mutex_unlock(&mSocketsMutex);
    pthread_mutex_destroy(&mSocketsMutex);
}

std::string ProfileSocketsRegister::createSocketForProfile(std::string const& profileName)
{
    LOG4CPLUS_TRACE_METHOD(mLogger, __PRETTY_FUNCTION__);
    pthread_mutex_lock(&mSocketsMutex);
    assert (mSockets.find(profileName) == mSockets.end());
    std::string address = NsUtils::ZMQ_PROTOCOL_PREFIX + profileName;
    mSockets[profileName] = new PMSenderZmqSocket(mZmqContext, address, NsUtils::BIND);
    pthread_mutex_unlock(&mSocketsMutex);
    return address;
}

void ProfileSocketsRegister::destroySocketForProfile(std::string const& profileName)
{
    LOG4CPLUS_TRACE_METHOD(mLogger, __PRETTY_FUNCTION__);
    pthread_mutex_lock(&mSocketsMutex);
    tSocketsMap::iterator iter = mSockets.find(profileName);
    if (iter != mSockets.end())
    {
        delete iter->second;
        mSockets.erase(iter);
    }
    else
    {
        LOG4CPLUS_ERROR(mLogger, "no socket for " + profileName + ", nothing to erase");
    }
    pthread_mutex_unlock(&mSocketsMutex);
}


void ProfileSocketsRegister::sendMessageToProfile(std::string const& profileName, const int appId,
        const MessageTypes type, int state, int protocolVersion,
        const int dataSize, const char * data)
{
    LOG4CPLUS_TRACE_METHOD(mLogger, __PRETTY_FUNCTION__);
    pthread_mutex_lock(&mSocketsMutex);
    tSocketsMap::iterator iter = mSockets.find(profileName);
    if (iter != mSockets.end())
    {
        iter->second->pushMessageToQueue(appId, type, state, protocolVersion, dataSize, data);
    }
    else
    {
        LOG4CPLUS_WARN(mLogger, "no socket for " + profileName + ", cannot send anything");
    }
    pthread_mutex_unlock(&mSocketsMutex);
}
