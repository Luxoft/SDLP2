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


#include "profile_manager/active_profiles_register.h"

using namespace NsProfileManager;

Logger ActiveProfilesRegister::mLogger = Logger::getInstance("SDL.ProfileManager.ActiveProfilesRegister");

ActiveProfilesRegister::ActiveProfilesRegister()
{
    LOG4CPLUS_TRACE_METHOD(mLogger, __PRETTY_FUNCTION__);
    pthread_mutex_init(&mProfilesMutex, 0);
}

ActiveProfilesRegister::~ActiveProfilesRegister()
{
    LOG4CPLUS_TRACE_METHOD(mLogger, __PRETTY_FUNCTION__);
    pthread_mutex_destroy(&mProfilesMutex);
}

bool ActiveProfilesRegister::isProfileActive(std::string const& profileID)
{
    LOG4CPLUS_TRACE_METHOD(mLogger, __PRETTY_FUNCTION__);
    pthread_mutex_lock(&mProfilesMutex);
    std::map<std::string, pid_t>::iterator iter = mProfiles.find(profileID);
    bool result = (iter != mProfiles.end());
    pthread_mutex_unlock(&mProfilesMutex);
    return result;
}

bool ActiveProfilesRegister::addProfileRecord(std::string const& profileID, pid_t pid)
{
    LOG4CPLUS_TRACE_METHOD(mLogger, __PRETTY_FUNCTION__);
    LOG4CPLUS_INFO(mLogger, "Trying to add " + profileID + " with pid " << pid);
    bool didInsert = false;
    pthread_mutex_lock(&mProfilesMutex);
    std::map<std::string, pid_t>::iterator iter = mProfiles.find(profileID);
    if (iter == mProfiles.end())
    {
        mProfiles[profileID] = pid;
        didInsert = true;
    }
    else
    {
        LOG4CPLUS_ERROR(mLogger, "Profile already in the registry!: old pid = "
                        << iter->second << "; new pid " << pid);
    }
    pthread_mutex_unlock(&mProfilesMutex);
    return didInsert;
}

bool ActiveProfilesRegister::eraseProfileRecord(std::string const& profileID)
{
    LOG4CPLUS_TRACE_METHOD(mLogger, __PRETTY_FUNCTION__);
    bool didErase = false;
    pthread_mutex_lock(&mProfilesMutex);
    std::map<std::string, pid_t>::iterator iter = mProfiles.find(profileID);
    if (iter != mProfiles.end())
    {
        mProfiles.erase(iter);
        didErase = true;
    }
    else
    {
        LOG4CPLUS_WARN(mLogger, "Profile " + profileID + " is not in the registry");
    }
    pthread_mutex_unlock(&mProfilesMutex);
    return didErase;
}

pid_t ActiveProfilesRegister::getPidOfProfile(std::string const& profileID)
{
    LOG4CPLUS_TRACE_METHOD(mLogger, __PRETTY_FUNCTION__);
    pid_t result = -1;
    pthread_mutex_lock(&mProfilesMutex);
    std::map<std::string, pid_t>::iterator iter = mProfiles.find(profileID);
    if (iter != mProfiles.end())
    {
        result = iter->second;
    }
    else
    {
        LOG4CPLUS_WARN(mLogger, "Profile " + profileID + " is not in the registry");
    }
    pthread_mutex_unlock(&mProfilesMutex);
    return result;
}

std::string ActiveProfilesRegister::getProfileByPID(pid_t pid)
{
    LOG4CPLUS_TRACE_METHOD(mLogger, __PRETTY_FUNCTION__);
    std::string result("");
    pthread_mutex_lock(&mProfilesMutex);
    std::map<std::string, pid_t>::iterator iter = mProfiles.begin();
    while (iter != mProfiles.end())
    {
        if (iter->second == pid)
        {
            result = iter->first;
            LOG4CPLUS_INFO(mLogger, "Found " + result + " for pid " << pid);
            break;
        }
        iter ++;
    }
    pthread_mutex_unlock(&mProfilesMutex);
    return result;
}
