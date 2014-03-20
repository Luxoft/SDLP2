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


#include "profile_manager/profile_connections_register.h"

using namespace NsProfileManager;

Logger ProfileConnectionsRegister::mLogger = Logger::getInstance("SDL.ProfileManager.ProfileConnectionsRegister");

ProfileConnectionsRegister::ProfileConnectionsRegister()
{
    pthread_mutex_init(&mProfilesAndAppIDsMutex, 0);
}

ProfileConnectionsRegister::~ProfileConnectionsRegister()
{
    pthread_mutex_destroy(&mProfilesAndAppIDsMutex);
}


void ProfileConnectionsRegister::addProfileConnection(std::string const& profileID, int appID)
{
    LOG4CPLUS_TRACE_METHOD(mLogger, __PRETTY_FUNCTION__);
    pthread_mutex_lock(&mProfilesAndAppIDsMutex);
    mProfilesAndAppIDs[profileID].insert(appID);
    pthread_mutex_unlock(&mProfilesAndAppIDsMutex);
}

std::set<int> ProfileConnectionsRegister::getProfileConnections(std::string const& profileID)
{
    LOG4CPLUS_TRACE_METHOD(mLogger, __PRETTY_FUNCTION__);
    std::set<int> result;
    pthread_mutex_lock(&mProfilesAndAppIDsMutex);
    if (mProfilesAndAppIDs.find(profileID) != mProfilesAndAppIDs.end())
    {
        result = mProfilesAndAppIDs[profileID];
    }
    pthread_mutex_unlock(&mProfilesAndAppIDsMutex);
    return result;
}

void ProfileConnectionsRegister::eraseProfileConnections(std::string const& profileID)
{
    LOG4CPLUS_TRACE_METHOD(mLogger, __PRETTY_FUNCTION__);
    pthread_mutex_lock(&mProfilesAndAppIDsMutex);
    mProfilesAndAppIDs.erase(profileID);
    pthread_mutex_unlock(&mProfilesAndAppIDsMutex);
}

std::string ProfileConnectionsRegister::getProfileNameByAppId(const int appID)
{
    LOG4CPLUS_TRACE_METHOD(mLogger, __PRETTY_FUNCTION__);
    std::string result("");
    pthread_mutex_lock(&mProfilesAndAppIDsMutex);
    std::map<std::string, std::set<int>>::iterator profileIter = mProfilesAndAppIDs.begin();
    bool found = false;
    for (; profileIter != mProfilesAndAppIDs.end(); profileIter ++)
    {
        std::set<int>::iterator iter = profileIter->second.begin();
        for (; iter != profileIter->second.end(); iter ++)
        {
            if (*iter == appID)
            {
                LOG4CPLUS_INFO(mLogger, "Found match:  " + profileIter->first);
                result = profileIter->first;
                found = true;
                break;
            }
        }
        if (found)
        {
            break;
        }
    }
    pthread_mutex_unlock(&mProfilesAndAppIDsMutex);
    return result;
}

std::set<std::string> ProfileConnectionsRegister::eraseAppConnections(const int appID)
{
    LOG4CPLUS_TRACE_METHOD(mLogger, __PRETTY_FUNCTION__);
    std::set<std::string> result;
    pthread_mutex_lock(&mProfilesAndAppIDsMutex);
    std::map<std::string, std::set<int>>::iterator profileIter = mProfilesAndAppIDs.begin();
    for (; profileIter != mProfilesAndAppIDs.end(); profileIter ++)
    {
        if (profileIter->second.erase(appID) == 1)
        {
            result.insert(profileIter->first);
        }
    }
    pthread_mutex_unlock(&mProfilesAndAppIDsMutex);
    return result;
}
