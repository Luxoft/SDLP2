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

#include "TestEchoProfile.h"

using namespace NsProfileManager;

Logger TestEchoProfile::mLogger = Logger::getInstance(
                                      LOG4CPLUS_TEXT("SDL.TestEchoProfile"));

#define LOADMSG "KONICHIWA! WATASHI WA EKO!!!"

extern "C" IProfile *CreateProfile()
{
    return new TestEchoProfile;
}

extern "C" void DestroyProfile(IProfile *profile)
{
    delete profile;
}

TestEchoProfile::TestEchoProfile()
    : mCallbacks(NULL),
      mName("TestEchoProfile"),
      mAppId(0)
{
    LOG4CPLUS_TRACE_METHOD(mLogger, __PRETTY_FUNCTION__);
}

TestEchoProfile::~TestEchoProfile()
{
    LOG4CPLUS_TRACE_METHOD(mLogger, __PRETTY_FUNCTION__);
}

void TestEchoProfile::onMessageReceived(const int appId, const char *data, const int dataSize)
{
    LOG4CPLUS_TRACE_METHOD(mLogger, __PRETTY_FUNCTION__);

    LOG4CPLUS_INFO(mLogger, "Message received : " + std::string(data, dataSize));
    LOG4CPLUS_INFO(mLogger, "Message received : dataSize = " << dataSize);
    LOG4CPLUS_INFO(mLogger, "Message received : appId = " << appId);

    mCallbacks->sendProfileToAppMessage(appId, data, dataSize);
}

void TestEchoProfile::onAppStateChanged(const int appId, const MobileAppState state)
{
    LOG4CPLUS_TRACE_METHOD(mLogger, __PRETTY_FUNCTION__);
    std::string response;
    if (mCallbacks)
    {
        switch (state)
        {
        case MOBILE_APP_BACKGROUND:
            LOG4CPLUS_INFO(mLogger, "MOBILE_APP_BACKGROUND received");
            response = "MOBILE_APP_BACKGROUND";
            break;
        case MOBILE_APP_FOREGROUND:
            LOG4CPLUS_INFO(mLogger, "MOBILE_APP_FOREGROUND received");
            response = "MOBILE_APP_FOREGROUND";
            break;
        case MOBILE_APP_LOCK_SCREEN:
            LOG4CPLUS_INFO(mLogger, "MOBILE_APP_LOCK_SCREEN received");
            response = "MOBILE_APP_LOCK_SCREEN";
            break;
        default:
            return;
        }
        mCallbacks->sendProfileToAppMessage(appId, response.c_str(), response.size());
    }
}

void TestEchoProfile::onUnload(const int appId)
{
    LOG4CPLUS_TRACE_METHOD(mLogger, __PRETTY_FUNCTION__);
}

void TestEchoProfile::onLoaded(const int appId)
{
    LOG4CPLUS_TRACE_METHOD(mLogger, __PRETTY_FUNCTION__);
    if (mCallbacks)
    {
        mAppId = appId;
    }
}

void TestEchoProfile::setProfileManagerProxy(IProfileManagerProxy *callbacks)
{
    LOG4CPLUS_TRACE_METHOD(mLogger, __PRETTY_FUNCTION__);

    if (callbacks)
    {
        mCallbacks = callbacks;
    }
}
