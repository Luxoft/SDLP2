/*
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


#include <dlfcn.h>
#include <cassert>
#include <iostream>
#include <cstring>
#include <zmq.h>

#include "profile_manager/profile_process/profile_process.h"
#include "profile_manager/appman_hmi_protocol/profiles_apps_message.h"
#include "zmq_socket/zmq_socket_common.h"

using namespace NsProfileManager;

Logger ProfileProcess::mLogger =
    Logger::getInstance(LOG4CPLUS_TEXT("SDL.ProfileManager.ProfileProcess"));

#define LOG4CPLUS_ERROR_WITH_ERRNO(logger, message) LOG4CPLUS_ERROR(logger, message << ", error code " << errno << " (" << strerror(errno) << ")")

ProfileProcess::ProfileProcess(const std::string &profileName, const std::string &pathToProfiles)
    : mProfileImpl(NULL),
      mCreateProfileFunc(NULL),
      mDestroyProfileFunc(NULL),
      mProfileName(profileName),
      mPathToProfiles(pathToProfiles)
{
    LOG4CPLUS_TRACE_METHOD(mLogger, __PRETTY_FUNCTION__);

    mZmqContext = zmq_ctx_new();

    if (mZmqContext)
    {
        mSender = new PMSenderZmqSocket(mZmqContext, NsUtils::ZMQ_PROTOCOL_PREFIX + PROFILE_MAN_REPLY_SOCKET, NsUtils::CONNECT);
        mReceiver = new PMReceiverZmqSocket(mZmqContext, NsUtils::ZMQ_PROTOCOL_PREFIX + mProfileName, NsUtils::CONNECT, this);
        sem_init(&mStopSema, 0, 0);
        mainLoop();
    }
    else
    {
        LOG4CPLUS_ERROR(mLogger, "Error getting 0MQ context");
        return;
    }
}

ProfileProcess::~ProfileProcess()
{
    LOG4CPLUS_TRACE_METHOD(mLogger, __PRETTY_FUNCTION__);
    delete mSender;
    delete mReceiver;
    zmq_ctx_destroy(mZmqContext);
}

void ProfileProcess::mainLoop()
{
    LOG4CPLUS_TRACE_METHOD(mLogger, __PRETTY_FUNCTION__);
    sem_wait(&mStopSema);
}

void ProfileProcess::loadProfile(const int appId)
{
    LOG4CPLUS_TRACE_METHOD(mLogger, __PRETTY_FUNCTION__);

    std::string path = mPathToProfiles + "lib" + mProfileName + ".so";

    LOG4CPLUS_INFO(mLogger, "Loading profile: " + path);
    mLibHandle = dlopen(path.c_str(), RTLD_NOW);
    if (!mLibHandle)
    {
        LOG4CPLUS_ERROR(mLogger, "Can't load profile library. dlopen failed");
        LOG4CPLUS_INFO(mLogger, "dlerror: " + std::string(dlerror()));
    }
    assert(mLibHandle);

    mCreateProfileFunc = (CreateProfileFunc *) dlsym(mLibHandle, CREATE_PROFILE_FUNC_NAME);
    assert(mCreateProfileFunc);
    if (!mCreateProfileFunc)
    {
        LOG4CPLUS_ERROR(mLogger, "Can't get CreateProfileFunc from profile library");
        return;
    }

    mDestroyProfileFunc = (DestroyProfileFunc *) dlsym(mLibHandle, DESTROY_PROFILE_FUNC_NAME);
    assert(mDestroyProfileFunc);
    if (!mDestroyProfileFunc)
    {
        LOG4CPLUS_ERROR(mLogger, "Can't get DestroyProfileFunc from profile library");
        return;
    }

    mProfileImpl = mCreateProfileFunc();
    mProfileImpl->setProfileManagerProxy(this);
    mProfileImpl->onLoaded(appId);
}

void ProfileProcess::unloadProfile(const int appId)
{
    LOG4CPLUS_TRACE_METHOD(mLogger, __PRETTY_FUNCTION__);

    if (mProfileImpl)
    {
        mProfileImpl->onUnload(appId);
    }
    stopOperations();
}

void ProfileProcess::stopOperations()
{
    LOG4CPLUS_TRACE_METHOD(mLogger, __PRETTY_FUNCTION__);

    if (mProfileImpl)
    {
        mDestroyProfileFunc(mProfileImpl);
        mProfileImpl = NULL;
    }
    else
    {
        LOG4CPLUS_ERROR(mLogger, "Profile pointer == 0 (" + mProfileName + ")");
    }

    if (dlclose(mLibHandle) != 0)
    {
        LOG4CPLUS_ERROR(mLogger, "Profile library closing error (Profile : " + mProfileName + ")");
    }

    sem_post(&mStopSema);
}

void ProfileProcess::sendAppToProfileMessage(const int appId,
        const std::string &profileName,
        const char * data,
        const int dataSize)
{
    LOG4CPLUS_TRACE_METHOD(mLogger, __PRETTY_FUNCTION__);
    if (mProfileImpl)
    {
        mProfileImpl->onMessageReceived(appId, data, dataSize);
    }
    else
    {
        LOG4CPLUS_ERROR(mLogger, "Profile pointer == 0 (" + profileName + ")");
    }
}

void ProfileProcess::appStateChanged(const MobileAppState state, const int appId)
{
    LOG4CPLUS_TRACE_METHOD(mLogger, __PRETTY_FUNCTION__);

    if (mProfileImpl)
    {
        mProfileImpl->onAppStateChanged(appId, state);
    }
    else
    {
        LOG4CPLUS_ERROR(mLogger, "Profile pointer == 0");
    }
}

void ProfileProcess::onAppManHmiNotification(const int appId, const char * data, const int dataSize)
{
    LOG4CPLUS_TRACE_METHOD(mLogger, __PRETTY_FUNCTION__);
    ProfilesAppsMessage message(std::string(data, dataSize));
    if (mProfileImpl)
    {
        mProfileImpl->onMessageFromHmi(message.getApplicationName(), message.getDataPayload(), message.getDataPayloadSize());
    }
}

/*
 * From IProfileCallbacks
 */
void ProfileProcess::sendProfileToAppMessage(const int appId,
        const char * data,
        const int dataSize)
{
    LOG4CPLUS_TRACE_METHOD(mLogger, __PRETTY_FUNCTION__);
    mSender->pushMessageToQueue(appId, MSG_TYPE_PROFILE_TO_APP_MESSAGE,
                                0, 0, dataSize, data);
}

void ProfileProcess::broadcastProfileToAppMessage(const char * data,
        const int dataSize)
{
    LOG4CPLUS_TRACE_METHOD(mLogger, __PRETTY_FUNCTION__);
    mSender->pushMessageToQueue(0, MSG_TYPE_PROFILE_TO_APP_MESSAGE_BROADCAST,
                                0, 0, dataSize, data);
}

void ProfileProcess::sendHmiRequest(std::string const& applicationName, const char * data, const int dataSize)
{
    LOG4CPLUS_TRACE_METHOD(mLogger, __PRETTY_FUNCTION__);

    if (applicationName.empty())
    {
        LOG4CPLUS_ERROR(mLogger, "Application name is empty!");
        return;
    }
    ProfilesAppsMessage msg;
    msg.setProfileName(mProfileName);
    msg.setApplicationName(applicationName);
    msg.setDataPayload(data, dataSize);
    std::string serializedMessage = msg.serialize();

    mSender->pushMessageToQueue(0, MSG_TYPE_HMI_REQUEST_IVILINK,
                                0, 0, serializedMessage.size(), serializedMessage.c_str());
}

void ProfileProcess::profileClosed()
{
    LOG4CPLUS_TRACE_METHOD(mLogger, __PRETTY_FUNCTION__);
    // profile manager will handle the closing by itself, nothing is sent
    stopOperations();
}

void ProfileProcess::handlePmMessage(ZmqMessageHeader header, const char * data)
{
    LOG4CPLUS_TRACE_METHOD(mLogger, __PRETTY_FUNCTION__);
    switch (header.messageType)
    {
    case MSG_TYPE_LOAD_PROFILE:
        loadProfile(header.appId);
        break;
    case MSG_TYPE_UNLOAD_PROFILE:
        unloadProfile(header.appId);
        break;
    case MSG_TYPE_APP_STATE_CHANGED:
        appStateChanged((MobileAppState)header.state, header.appId);
        break;
    case MSG_TYPE_APP_TO_PROFILE_MESSAGE:
        sendAppToProfileMessage(header.appId,
                                mProfileName,
                                data,
                                header.dataSize);
        break;
    case MSG_TYPE_MOBILE_APP_EXITED:
        if (mProfileImpl)
        {
            mProfileImpl->onMobileAppDisconnected(header.appId);
        }
        break;
    case MSG_TYPE_HMI_NOTIFICATION_IVILINK:
        onAppManHmiNotification(header.appId, data, header.dataSize);
        break;
    default:
        LOG4CPLUS_WARN(mLogger, "Message to profile: incorrect type!");
        break;
    }
}