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


#ifndef PROFILEPROCESS_H
#define PROFILEPROCESS_H

#include <list>
#include <pthread.h>
#include <semaphore.h>

#include "Logger.hpp"

#include "profileApi/IProfile.h"
#include "profileApi/IProfileManagerProxy.h"
#include "profile_manager/ipc/pm_receiver_zmq_socket.h"
#include "profile_manager/ipc/pm_sender_zmq_socket.h"

/**
  *\namespace NsProfileManager
  *\brief Namespace for SmartDeviceLink ProfileManager related functionality.
*/
namespace NsProfileManager
{

class ProfileProcess : public IProfileManagerProxy, public IPMSocketListener
{
public:
    ProfileProcess(const std::string &profileName, const std::string &pathToProfiles);

    virtual ~ProfileProcess();

private:
    void loadProfile (const int appId);

    void unloadProfile (const int appId);

    void sendAppToProfileMessage (const int appId,
                                  const std::string & profileName,
                                  const char * data,
                                  const int dataSize);

    void appStateChanged (MobileAppState state, const int appId);

    void mainLoop();

    // from IClientListener

    virtual void handlePmMessage(ZmqMessageHeader header, const char * data);

    // From IProfileManagerProxy

    virtual void sendProfileToAppMessage(const int appId,
                                         const char * data,
                                         const int dataSize);
    virtual void broadcastProfileToAppMessage(const char * data,
            const int dataSize) ;

    virtual void profileClosed();

    virtual void sendHmiRequest(std::string const& applicationName, const char * data, const int dataSize);
private:
    void stopOperations();

    void onAppManHmiNotification(const int appId, const char * data, const int dataSize);

private:
    std::string mProfileName;
    std::string mPathToProfiles;

    PMSenderZmqSocket * mSender;
    PMReceiverZmqSocket * mReceiver;

    IProfile *mProfileImpl;
    CreateProfileFunc *mCreateProfileFunc;
    DestroyProfileFunc *mDestroyProfileFunc;
    void *mLibHandle;
    sem_t mStopSema;

    static Logger mLogger;

    void * mZmqContext;
};

} //NsProfileManager

#endif //PROFILEPROCESS_H
