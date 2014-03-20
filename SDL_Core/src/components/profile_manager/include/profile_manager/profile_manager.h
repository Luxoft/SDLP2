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


#ifndef PROFILEMANAGER_H
#define PROFILEMANAGER_H

#include "Logger.hpp"

#include "profile_manager/isdl_core_adapter.h"
#include "application_manager_profile_manager_interface/iprofile_manager.h"
/**
  *\namespace NsProfileManager
  *\brief Namespace for SmartDeviceLink ProfileManager related functionality.
*/
namespace NsProfileManager
{

class ProfileInstanceManager;
class ApplicationManagerHmiCommunicator;

/**
 * @brief The ProfileManager class provides profile managing functionality and communication between
 * SDL core and profiles
 */
class ProfileManager : public profile_manager::IProfileManager
{

public:
    ProfileManager(const std::string & pathToProfiles);

    ~ProfileManager();

    virtual ProfileManagerResult loadProfile(const int appId, const std::string & name);
    virtual ProfileManagerResult unloadProfile(const int appId, const std::string & name);
    virtual ProfileManagerResult sendAppToProfileMessage(const int appId,
            const std::string & profileName,
            const char * data,
            const int dataSize);
    virtual ProfileManagerResult addProfile(const int appId, const std::string & profileName,
                                            const char * profileBin,
                                            const int profileSize);
    virtual ProfileManagerResult removeProfile(const int appId, const std::string & profileName);
    virtual ProfileManagerResult appStateChanged(const int appId, const std::string & profileName,
            const mobile_apis::MobileAppState::eType  state);
    virtual void onMobileAppExited(const int appId);

    // From IProfileCallbacks

    /**
     * @brief sendProfileToAppMessage Send message from profile to the mobile application
     * @param profileName Profile name
     * @param appId Application id
     * @param data Pointer to the data array
     * @param dataSize Data array size
     */
    virtual void sendProfileToAppMessage(const std::string & profileName,
                                         const int appId,
                                         const char * data,
                                         const int dataSize);

    /**
     * @brief profileClosed Send notification about profile closing
     * @param profileName Profile name
     * @param appId Application id
     */
    virtual void profileClosed(const std::string & profileName, const int appId);

    virtual void profileAppManHmiRequest(const char * data, const unsigned int dataSize);

    /**
     * @brief setProfileManagerAdapter sets pointer on profile manager adapter
     * @param profileMgrAdapter is pointer on base class of of profile manager adapter instance
     */
    void setSDLCoreAdapter(ISDLCoreAdapter * profileMgrAdapter);

private:
    ProfileInstanceManager * mProfileInstanceManager;
    ApplicationManagerHmiCommunicator * mAppManCommunicator;
    ISDLCoreAdapter * mSdlCoreAdapter;
    std::string mPathToProfiles;
    static Logger mLogger;
};

} //NsProfileManager

#endif //PROFILEMANAGER_H
