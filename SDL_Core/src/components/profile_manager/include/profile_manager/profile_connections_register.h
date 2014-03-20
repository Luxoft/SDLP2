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


#ifndef __PROFILE_CONNECTIONS_REGISTER_H__
#define __PROFILE_CONNECTIONS_REGISTER_H__

#include <map>
#include <set>
#include <string>

#include "Logger.hpp"

namespace NsProfileManager
{
/**
 * \brief Stores info about mobile applications that have communicated with the profile.
 */
class ProfileConnectionsRegister
{
public:
    ProfileConnectionsRegister();
    ~ProfileConnectionsRegister();

    void addProfileConnection(std::string const& profileID, int appID);

    /**
     * @return set of Application ID's that can be used to send notifications to.
     */
    std::set<int> getProfileConnections(std::string const& profileID);

    void eraseProfileConnections(std::string const& profileID);

    /**
     * @return set of profiles that should be notified
     */
    std::set<std::string> eraseAppConnections(const int appID);

    /**
     * @brief find a profile that has communicated with this app
     * @return profile ID or an empty string, if nothing has been found
     */
    std::string getProfileNameByAppId(const int appID);

private:
    std::map<std::string, std::set<int>> mProfilesAndAppIDs;
    pthread_mutex_t mProfilesAndAppIDsMutex;

    static Logger mLogger;
};

}

#endif //__PROFILE_CONNECTIONS_REGISTER_H__
