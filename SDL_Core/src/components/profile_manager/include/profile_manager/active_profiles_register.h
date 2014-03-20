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


#ifndef __ACTIVE_PROFILES_REGISTER_H__
#define __ACTIVE_PROFILES_REGISTER_H__

#include <pthread.h>
#include <map>
#include <list>
#include <string>

#include "Logger.hpp"

namespace NsProfileManager
{

/**
 * \brief Stores and provides info about loaded profiles:
 * their names and process ID's
 */
class ActiveProfilesRegister
{

public:
    ActiveProfilesRegister();
    ~ActiveProfilesRegister();

    /**
     * @return true if profile with this ID is active
     */
    bool isProfileActive(std::string const& profileID);

    /**
     * @return false if profile with this ID is in the registry already
     */
    bool addProfileRecord(std::string const& profileID, pid_t pid);

    /**
     * @return false if there was no record with this ID
     */
    bool eraseProfileRecord(std::string const& profileID);

    /**
     * @return PID of profile (provided in a previous call to addProfileToRegistry),
     * or -1 if there was no record with this ID
     */
    pid_t getPidOfProfile(std::string const& profileID);

    /**
     * @return ProfileName if there is a record of a ProfileProcess with this pid,
     * or an empty string otherwise.
     */
    std::string getProfileByPID(pid_t pid);

private:
    std::map<std::string, pid_t> mProfiles;
    pthread_mutex_t mProfilesMutex;

    static Logger mLogger;
};

}

#endif //__ACTIVE_PROFILES_REGISTER_H__
