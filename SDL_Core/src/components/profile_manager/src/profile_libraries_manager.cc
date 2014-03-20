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


#include <stdio.h>
#include <sys/stat.h>
#include <fstream>

#include "profile_manager/profile_libraries_manager.h"
#include "utils/work_with_os.h"

#define FILE_PERMISSIONS 0755

using namespace NsProfileManager;


Logger ProfileLibrariesManager::mLogger = Logger::getInstance("SDL.ProfileManager.ProfileLibrariesManager");

static std::string savedFilePath("");

ProfileLibrariesManager::ProfileLibrariesManager()
{
    LOG4CPLUS_TRACE_METHOD(mLogger, __PRETTY_FUNCTION__);
    pthread_mutex_init(&mMutex, 0);
}

ProfileLibrariesManager::~ProfileLibrariesManager()
{
    LOG4CPLUS_TRACE_METHOD(mLogger, __PRETTY_FUNCTION__);
    if (!savedFilePath.empty())
    {
        pthread_mutex_lock(&mMutex);
        tIncompleteProfileLibsMap::iterator iter = mIncompleteLibsInfo.begin();
        while (iter != mIncompleteLibsInfo.end())
        {
            eraseFile(genTempPath(savedFilePath, iter->first.first, iter->first.second));
            iter ++;
        }
        mIncompleteLibsInfo.clear();
        pthread_mutex_unlock(&mMutex);
    }
    pthread_mutex_destroy(&mMutex);
}

ProfileLibrariesManager& ProfileLibrariesManager::getInstance()
{
    static ProfileLibrariesManager instance;
    return instance;
}

ProfileManagerResult ProfileLibrariesManager::appendProfileData(std::string const& profileName, std::string const& filePath,
        const char * data, const int dataSize, const int frameNumber, const int totalFrames,
        const int appId)
{
    LOG4CPLUS_TRACE_METHOD(mLogger, __PRETTY_FUNCTION__);
    pthread_mutex_lock(&mMutex);
    savedFilePath = filePath;
    tProfileNameAndAppId key = std::make_pair(profileName, appId);
    tIncompleteProfileLibsMap::iterator iter = mIncompleteLibsInfo.find(key);
    LOG4CPLUS_INFO(mLogger, "Frame number: " << frameNumber  << ", totalFrames: " << totalFrames);
    if (iter == mIncompleteLibsInfo.end())
    {
        if (frameNumber != 0)
        {
            LOG4CPLUS_ERROR(mLogger, "Non-initial frame number for non-existent profile " + profileName);
            pthread_mutex_unlock(&mMutex);
            return ProfileManagerResult(mobile_apis::Result::INVALID_DATA, "Wrong order of frames");
        }
        WorkWithOS::createDirectory(filePath);
        eraseFile(genTempPath(filePath, profileName, appId));
        eraseFile(genPathToProfile(filePath, profileName));
        mIncompleteLibsInfo.insert(std::make_pair(key, IncompleteProfileLibData(totalFrames)));
    }
    else
    {
        if (iter->second.latestFrameNumber + 1 != frameNumber)
        {
            LOG4CPLUS_ERROR(mLogger, "Wrong frame sequence: " + profileName + " expected: "
                            << (iter->second.latestFrameNumber + 1) << " got: " << frameNumber);
            pthread_mutex_unlock(&mMutex);
            return ProfileManagerResult(mobile_apis::Result::INVALID_DATA, "Wrong order of frames");
        }
        mIncompleteLibsInfo[key].latestFrameNumber = frameNumber;
    }
    LOG4CPLUS_INFO(mLogger, "Got for profile: " + profileName << " frameNumber: " << frameNumber
                   << " frameSize: " << dataSize);
    appendToFile(genTempPath(filePath, profileName, appId), data, dataSize);
    if (frameNumber == totalFrames - 1)
    {
        finalizeProfile(filePath, profileName, appId);
        mIncompleteLibsInfo.erase(key);
    }
    pthread_mutex_unlock(&mMutex);
    return ProfileManagerResult(mobile_apis::Result::SUCCESS);
}

ProfileManagerResult ProfileLibrariesManager::eraseLibraryForProfile(std::string const& filePath,
        std::string const& profileName)
{
    LOG4CPLUS_TRACE_METHOD(mLogger, __PRETTY_FUNCTION__);
    ProfileManagerResult result(mobile_apis::Result::SUCCESS);
    pthread_mutex_lock(&mMutex);
    if (!eraseFile(genPathToProfile(filePath, profileName)))
    {
        result = ProfileManagerResult(mobile_apis::Result::INVALID_ID, "No library to remove for profile " + profileName);
    }
    pthread_mutex_unlock(&mMutex);
    return result;
}

bool ProfileLibrariesManager::hasLibraryForProfile(std::string const& filePath, std::string const& profileName)
{
    LOG4CPLUS_TRACE_METHOD(mLogger, __PRETTY_FUNCTION__);
    pthread_mutex_lock(&mMutex);
    LOG4CPLUS_INFO(mLogger, "gen path: " + genPathToProfile(filePath, profileName));
    bool result = WorkWithOS::checkIfFileExists(genPathToProfile(filePath, profileName));
    pthread_mutex_unlock(&mMutex);
    return result;
}

void ProfileLibrariesManager::removeIncompleteDataFromApp(const int appId, std::string const& filePath)
{
    LOG4CPLUS_TRACE_METHOD(mLogger, __PRETTY_FUNCTION__);
    pthread_mutex_lock(&mMutex);
    tIncompleteProfileLibsMap::iterator iter = mIncompleteLibsInfo.begin();
    while (iter != mIncompleteLibsInfo.end())
    {
        tIncompleteProfileLibsMap::iterator current = iter++;
        if (current->first.second == appId)
        {
            LOG4CPLUS_INFO(mLogger, "Found incomplete profile " + current->first.first + " for " << appId);
            eraseFile(genTempPath(filePath, current->first.first, appId));
            mIncompleteLibsInfo.erase(current);
        }
    }
    pthread_mutex_unlock(&mMutex);
}

std::string ProfileLibrariesManager::genPathToProfile(std::string const& pathToFile,
        std::string const& profileName)
{
    return pathToFile + "lib" + profileName + ".so";
}

std::string ProfileLibrariesManager::genTempPath(std::string const& pathToFile, std::string const& profileName,
        const int appId)
{
    return pathToFile + "lib" + profileName + convertIntegerToString(appId) + ".tmp";
}

bool ProfileLibrariesManager::eraseFile(std::string const& fileName)
{
    LOG4CPLUS_INFO(mLogger, "Trying to erase " + fileName);
    return WorkWithOS::deleteFile(fileName);
}

void ProfileLibrariesManager::appendToFile(std::string const& fileName, const char * data, const int dataSize)
{
    LOG4CPLUS_INFO(mLogger, "Appending " << dataSize << " bytes to " + fileName);
    std::ofstream file;
    file.open(fileName.c_str(), std::ios::out | std::ios::app | std::ios::binary);
    file.write(data, dataSize);
    file.close();
}

void ProfileLibrariesManager::finalizeProfile(std::string const& pathToFile, std::string const& profileName,
        const int appId)
{
    LOG4CPLUS_INFO(mLogger, "Finalizing profile: " + profileName);
    std::string resultName = genPathToProfile(pathToFile, profileName);
    eraseFile(resultName);
    WorkWithOS::renameFile(genTempPath(pathToFile, profileName, appId), resultName);
    chmod(resultName.c_str(), FILE_PERMISSIONS);
}
