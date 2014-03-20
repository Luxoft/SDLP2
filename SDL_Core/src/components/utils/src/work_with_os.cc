/**
* \file WorkWithOS.cpp
* \brief Implementation of general functions for work with OS.
*/

#include <iostream>
#include <fstream>
#include <cstddef>
#include <algorithm>
#include <string>
#include <errno.h>
#include <sstream>
#include <sys/statvfs.h>
#include <sys/stat.h>
#include <sys/types.h>
#include <dirent.h>
#include <stdio.h>
#include <unistd.h>

#include "utils/work_with_os.h"

unsigned long int WorkWithOS::getAvailableSpace()
{
    char currentAppPath[256];
    memset((void*)currentAppPath, 0, 256);
    getcwd(currentAppPath, 255);

    struct statvfs fsInfo;
    memset((void*)&fsInfo, 0, sizeof(fsInfo));
    statvfs(currentAppPath, &fsInfo);
    return fsInfo.f_bsize * fsInfo.f_bfree;
}

std::string WorkWithOS::getFullPath(const std::string & fileName)
{
    char currentAppPath[FILENAME_MAX];
    memset(currentAppPath, 0, FILENAME_MAX);
    getcwd(currentAppPath, FILENAME_MAX);

    char path[FILENAME_MAX];
    memset(path, 0, FILENAME_MAX);
    snprintf(path, FILENAME_MAX - 1, "%s/%s"
             , currentAppPath, fileName.c_str());
    return std::string(path);
}

std::string WorkWithOS::createDirectory(const std::string & directoryName)
{
    if (!checkIfDirectoryExists(directoryName))
    {
        static const mode_t mode = S_IRWXU | S_IRWXG | S_IROTH | S_IXOTH;

        size_t pre = 0, pos;
        std::string dir;
        int mdret;
        std::string dirNameCopy(directoryName.c_str());

        if(dirNameCopy[dirNameCopy.size() - 1] != '/')
        {
            // force trailing / so we can handle everything in loop
            dirNameCopy += '/';
        }

        while ( (pos = dirNameCopy.find_first_of('/',pre)) != std::string::npos)
        {
            dir = dirNameCopy.substr(0, pos ++);
            pre = pos;
            if(dir.size() == 0)
            {
                continue; // if leading '/' first time is 0 length
            }

            if ((mdret = mkdir(dir.c_str(), mode)) && errno != EEXIST)
            {
                break;
            }
        }
    }

    return directoryName;
}


bool WorkWithOS::checkIfDirectoryExists(const std::string & directoryName)
{
    struct stat status;
    memset(&status, 0, sizeof(status));

    if (-1 == stat(directoryName.c_str(), &status)
            || !S_ISDIR(status.st_mode))
    {
        return false;
    }

    return true;
}

bool WorkWithOS::checkIfFileExists(const std::string & fileName)
{
    struct stat status;
    memset(&status, 0, sizeof(status));

    if (-1 == stat(fileName.c_str(), &status))
    {
        return false;
    }
    return true;
}

bool WorkWithOS::createFileAndWrite(const std::string & fileName,
                                    const std::vector<unsigned char>& fileData)
{
    std::ofstream file(fileName.c_str(), std::ios_base::binary);
    if (file.is_open())
    {
        for (int i = 0; i < fileData.size(); ++i)
        {
            file << fileData[i];
        }
        file.close();
        return true;
    }
    return false;
}

bool WorkWithOS::deleteFile(const std::string & fileName)
{
    if (checkIfFileExists(fileName))
    {
        return !remove(fileName.c_str());
    }
    return false;
}

std::vector<std::string> WorkWithOS::listFilesInDirectory(
    const std::string & directoryName)
{
    std::vector<std::string> listFiles;
    if (!checkIfDirectoryExists(directoryName))
    {
        return listFiles;
    }

    DIR * directory = NULL;
    struct dirent* dirElement = NULL;
    directory = opendir(directoryName.c_str());
    if ( NULL != directory )
    {
        while( dirElement = readdir(directory) )
        {
            if (0 == strcmp(dirElement->d_name, "..")
                    || 0 == strcmp(dirElement->d_name, "."))
            {
                continue;
            }
            listFiles.push_back(std::string(dirElement->d_name));
        }
        closedir(directory);
    }

    return listFiles;
}

bool WorkWithOS::readFileAsBinary(const std::string& fileName, std::vector<unsigned char>& v)
{
    if (!checkIfFileExists(fileName))
    {
        return false;
    }

    std::ifstream file(fileName.c_str(), std::ios_base::binary);
    std::ostringstream ss;
    ss << file.rdbuf();
    const std::string& s = ss.str();

    v.resize(s.length());
    std::copy(s.begin(), s.end(), v.begin());
    return true;
}

bool WorkWithOS::addToFile(const std::string & fileName, const std::vector<unsigned char>& fileData)
{
    if (!checkIfFileExists(fileName))
    {
        return false;
    }

    std::ofstream file(fileName.c_str(), std::ios_base::out | std::ios_base::app | std::ios_base::binary);
    if (file.is_open())
    {
        for (int i = 0; i < fileData.size(); ++i)
        {
            file << fileData[i];
        }
        file.close();
        return true;
    }
    return false;
}

bool WorkWithOS::renameFile(const std::string & fileName, const std::string & newFileName)
{
    if (!checkIfFileExists(fileName))
    {
        return false;
    }
    return rename (fileName.c_str(), newFileName.c_str());
}