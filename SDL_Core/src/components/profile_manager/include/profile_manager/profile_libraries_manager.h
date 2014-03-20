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


#ifndef __PROFILE_LIBRARIES_MANAGER_H__
#define __PROFILE_LIBRARIES_MANAGER_H__

#include <map>
#include <string>
#include <pthread.h>

#include "application_manager_profile_manager_interface/profile_manager_result.h"
#include "Logger.hpp"

namespace NsProfileManager
{
/**
 * \brief Manages creation and deletion of profile library files.
 */
class ProfileLibrariesManager
{
    struct IncompleteProfileLibData
    {
        int latestFrameNumber;
        int totalFrames;

        IncompleteProfileLibData()
        {
            latestFrameNumber = 0;
            totalFrames = 0;
        }

        IncompleteProfileLibData(int totalFrames)
        {
            latestFrameNumber = 0;
            totalFrames = totalFrames;
        }
    };

    typedef std::pair<std::string, int> tProfileNameAndAppId;
    typedef std::map<tProfileNameAndAppId, IncompleteProfileLibData> tIncompleteProfileLibsMap;

    ProfileLibrariesManager();
    ~ProfileLibrariesManager();
    ProfileLibrariesManager(const ProfileLibrariesManager&);
    ProfileLibrariesManager& operator=(ProfileLibrariesManager const&);

public:
    static ProfileLibrariesManager& getInstance();

    /**
     * Appends data to profile library temporary file (creating it if necessary), preserving
     * correct order of frames. If frame is the last one, a *.so file ready to be loaded is created.
     * @return INVALID_DATA - if frame is out-of-order, SUCCESS otherwise
     */
    ProfileManagerResult appendProfileData(std::string const& profileName, std::string const& filePath,
                                           const char * data, const int dataSize, const int frameNumber, const int totalFrames,
                                           const int appId);

    /**
     * Checks if there is a *.so file for profile in the filePath folder.
     */
    bool hasLibraryForProfile(std::string const& filePath, std::string const& profileName);

    /**
     * Erases profile library file if there is one in the filePath directory.
     * @return INVALID_ID if there is no file, SUCCESS otherwise
     */
    ProfileManagerResult eraseLibraryForProfile(std::string const& filePath, std::string const& profileName);

    /**
     * If the app has started uploading the profile binary, and then exited unexpectedly,
     * this will clean up all temporary files associated with this application.
     */
    void removeIncompleteDataFromApp(const int appId, std::string const& filePath);

    std::string genPathToProfile(std::string const& pathToFile, std::string const& profileName);

private:
    bool eraseFile(std::string const& fileName);
    void appendToFile(std::string const& fileName, const char * data, const int dataSize);
    void finalizeProfile(std::string const& pathToFile, std::string const& profileName, const int appId);

    std::string genTempPath(std::string const& pathToFile, std::string const& profileName, const int appId);
private:
    tIncompleteProfileLibsMap mIncompleteLibsInfo;
    pthread_mutex_t mMutex;

    static Logger mLogger;
};

}

#endif //__PROFILE_LIBRARIES_MANAGER_H__
