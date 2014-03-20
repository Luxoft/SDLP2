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


#include <sys/prctl.h>
#include <signal.h>

#include "profile_manager/profile_process/profile_process.h"

using namespace NsProfileManager;

int main(int argc, char** argv)
{
    // Log4cplus initialization
    Logger logger = Logger::getInstance(LOG4CPLUS_TEXT("SDL.ProfileManager.ProfileProcess.Main"));
    PropertyConfigurator::doConfigure(LOG4CPLUS_TEXT("log4cplus.properties"));

    // ProfileProcess will be killed when parent process terminates
    prctl(PR_SET_PDEATHSIG, SIGTERM);

    ProfileProcess *process = NULL;

    if (argc >= 3)
    {
        LOG4CPLUS_INFO(logger, "Creating ProfileProcess object for profile: "
                       + std::string(argv[1]));
        process = new ProfileProcess(std::string(argv[1]), std::string(argv[2]));
        delete process;
    }
    else
    {
        LOG4CPLUS_ERROR(logger, "Incorrect process start (profile name argument is not provided)");
    }

    return 0;
}
