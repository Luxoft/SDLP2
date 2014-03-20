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


#include "TestSendingMsgProfile.h"

using namespace NsProfileManager;

Logger TestSendingMsgProfile::mLogger = Logger::getInstance(
        LOG4CPLUS_TEXT("SDL.TestSendingMsgProfile"));

#define AMOUNT_OF_MSG   50
#define MSG     " Kitsune"

extern "C" IProfile *CreateProfile()
{
    return new TestSendingMsgProfile;
}

extern "C" void DestroyProfile(IProfile *profile)
{
    delete profile;
}

TestSendingMsgProfile::TestSendingMsgProfile()
    : mCallbacks(NULL),
      mName("TestSendingMsgProfile"),
      mAppId(0)
{
    LOG4CPLUS_TRACE_METHOD(mLogger, __PRETTY_FUNCTION__);
}

TestSendingMsgProfile::~TestSendingMsgProfile()
{
    LOG4CPLUS_TRACE_METHOD(mLogger, __PRETTY_FUNCTION__);
}

void TestSendingMsgProfile::onMessageReceived(const int appId, const char *data, const int dataSize)
{
    LOG4CPLUS_TRACE_METHOD(mLogger, __PRETTY_FUNCTION__);

    LOG4CPLUS_INFO(mLogger, "Message received : " + std::string(data, dataSize));
    LOG4CPLUS_INFO(mLogger, "Message received : dataSize = " << dataSize);
    LOG4CPLUS_INFO(mLogger, "Message received : appId = " << appId);

    if (std::string(data, dataSize).compare("SEND_ME_NULL") == 0)
    {
        LOG4CPLUS_INFO(mLogger, "SEND_ME_NULL received, sendin message with null data");
        sendNullMsgToApp();
    }
    else if (std::string(data, dataSize).compare("UNLOAD") == 0)
    {
        mCallbacks->profileClosed();
    }
}

void TestSendingMsgProfile::onUnload(const int appId)
{
    LOG4CPLUS_TRACE_METHOD(mLogger, __PRETTY_FUNCTION__);
}

void TestSendingMsgProfile::onLoaded(const int appId)
{
    LOG4CPLUS_TRACE_METHOD(mLogger, __PRETTY_FUNCTION__);
    mAppId = appId;
}


void TestSendingMsgProfile::setProfileManagerProxy(IProfileManagerProxy *callbacks)
{
    LOG4CPLUS_TRACE_METHOD(mLogger, __PRETTY_FUNCTION__);

    if (callbacks)
    {
        mCallbacks = callbacks;
    }
}

void TestSendingMsgProfile::sendNullMsgToApp()
{
    LOG4CPLUS_TRACE_METHOD(mLogger, __PRETTY_FUNCTION__);
    mCallbacks->sendProfileToAppMessage(mAppId, "", sizeof(""));
}
