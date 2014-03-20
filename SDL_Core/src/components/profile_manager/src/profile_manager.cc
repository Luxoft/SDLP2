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


#include <arpa/inet.h>
#include <string.h>

#include "utils/base_64_coder.h"
#include "profile_manager/profile_manager.h"
#include "profile_manager/profile_libraries_manager.h"
#include "profile_manager/profile_instance_manager.h"
#include "profile_manager/application_manager_hmi_communicator.h"


using namespace NsProfileManager;

using namespace mobile_apis;

Logger ProfileManager::mLogger = Logger::getInstance(
                                     LOG4CPLUS_TEXT("SDL.ProfileManager.ProfileManager"));

ProfileManager::ProfileManager(const std::string & pathToProfiles)
    : mSdlCoreAdapter(NULL)
    , mPathToProfiles(pathToProfiles)
{
    PropertyConfigurator::doConfigure("log4cplus.properties");
    LOG4CPLUS_TRACE_METHOD(mLogger, __PRETTY_FUNCTION__);
    mProfileInstanceManager = new ProfileInstanceManager(mPathToProfiles);
    mProfileInstanceManager->setProfileManager(this);
    mAppManCommunicator = new ApplicationManagerHmiCommunicator(mProfileInstanceManager);
}

ProfileManager::~ProfileManager()
{
    LOG4CPLUS_TRACE_METHOD(mLogger, __PRETTY_FUNCTION__);
    delete mAppManCommunicator;
    delete mProfileInstanceManager;
}

ProfileManagerResult ProfileManager::loadProfile(const int appId, const std::string & name)
{
    LOG4CPLUS_TRACE_METHOD(mLogger, __PRETTY_FUNCTION__);
    return mProfileInstanceManager->loadProfile(name, appId);
}

ProfileManagerResult ProfileManager::unloadProfile(const int appId, const std::string & name)
{
    LOG4CPLUS_TRACE_METHOD(mLogger, __PRETTY_FUNCTION__);
    return mProfileInstanceManager->unloadProfile(name, appId);
}

ProfileManagerResult ProfileManager::sendAppToProfileMessage(const int appId,
        const std::string & profileName,
        const char * data,
        const int dataSize)
{
    ProfileManagerResult result = mProfileInstanceManager->sendAppToProfileMessage(appId, profileName,
                                  data, dataSize);
    return result;
}

ProfileManagerResult ProfileManager::addProfile(const int appId, const std::string & profileName,
        const char * profileBin,
        const int profileSize)
{
    LOG4CPLUS_TRACE_METHOD(mLogger, __PRETTY_FUNCTION__);
    if (mProfileInstanceManager->isProfileActive(profileName))
    {
        LOG4CPLUS_WARN(mLogger, "Profile " + profileName + " is active, cannot add");
        return ProfileManagerResult(Result::IN_USE, "Profile " + profileName + " is currently active");
    }

    int numberOfAllFrames = 0;
    int currentFrame = 0;
    int offset = 0;

    memcpy(&numberOfAllFrames, profileBin, sizeof(int32_t));
    numberOfAllFrames = ntohl(numberOfAllFrames);
    offset += sizeof(int32_t);

    memcpy(&currentFrame, profileBin + offset, sizeof(int32_t));
    currentFrame = ntohl(currentFrame);
    offset += sizeof(int32_t);

    ProfileManagerResult retval = ProfileLibrariesManager::getInstance().appendProfileData(profileName, mPathToProfiles,
                                  profileBin + offset, profileSize - offset, currentFrame, numberOfAllFrames,
                                  appId);
    return retval;
}

ProfileManagerResult ProfileManager::removeProfile(const int appId, const std::string & profileName)
{
    LOG4CPLUS_TRACE_METHOD(mLogger, __PRETTY_FUNCTION__);
    if (!mProfileInstanceManager->isProfileActive(profileName))
    {
        return ProfileLibrariesManager::getInstance().eraseLibraryForProfile(mPathToProfiles, profileName);
    }
    else
    {
        LOG4CPLUS_WARN(mLogger, "Profile " + profileName + " is active!");
        return ProfileManagerResult(Result::IN_USE, "Profile " + profileName + " is currently active");
    }
}

void ProfileManager::onMobileAppExited(const int appId)
{
    LOG4CPLUS_TRACE_METHOD(mLogger, __PRETTY_FUNCTION__);
    ProfileLibrariesManager::getInstance().removeIncompleteDataFromApp(appId, mPathToProfiles);
    mProfileInstanceManager->onMobileAppExited(appId);
}

ProfileManagerResult ProfileManager::appStateChanged(const int appId, const std::string & profileName,
        const mobile_apis::MobileAppState::eType state )
{
    LOG4CPLUS_TRACE_METHOD(mLogger, __PRETTY_FUNCTION__);
    NsProfileManager::MobileAppState castState;
    switch (state)
    {
    case mobile_apis::MobileAppState::MOBILE_APP_FOREGROUND:
        castState = NsProfileManager::MOBILE_APP_FOREGROUND;
        break;
    case mobile_apis::MobileAppState::MOBILE_APP_BACKGROUND:
        castState = NsProfileManager::MOBILE_APP_BACKGROUND;
        break;
    case mobile_apis::MobileAppState::MOBILE_APP_LOCK_SCREEN:
        castState = NsProfileManager::MOBILE_APP_LOCK_SCREEN;
        break;
    default:
        return ProfileManagerResult(Result::REJECTED);
    }
    return mProfileInstanceManager->appStateChanged(profileName, castState, appId);
}

/*
 * From IProfileCallbacks
 */

void ProfileManager::sendProfileToAppMessage(const std::string &profileName,
        const int appId,
        const char * data,
        const int dataSize)
{
    LOG4CPLUS_TRACE_METHOD(mLogger, __PRETTY_FUNCTION__);
    if (mSdlCoreAdapter)
    {
        mSdlCoreAdapter->sendMessageToApp(appId, profileName, data, dataSize);
    }
}

void ProfileManager::profileClosed(const std::string & profileName, const int appId)
{
    LOG4CPLUS_TRACE_METHOD(mLogger, __PRETTY_FUNCTION__);
    if (mSdlCoreAdapter)
    {
        mSdlCoreAdapter->sendProfileClosedNotification(appId, profileName);
    }
}


void ProfileManager::profileAppManHmiRequest(const char * data, const unsigned int dataSize)
{
    LOG4CPLUS_TRACE_METHOD(mLogger, __PRETTY_FUNCTION__);
    mAppManCommunicator->sendMessageToAppManager(data, dataSize);
}

void ProfileManager::setSDLCoreAdapter(ISDLCoreAdapter * profileMgrAdapter)
{
    mSdlCoreAdapter = profileMgrAdapter;
}

