/**
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
/**
 * @author Elena Bratanova <ebratanova@luxoft.com>
 */

#include <string.h>
#include "json/json.h"
#include "profile_manager/appman_hmi_protocol/profiles_apps_message.h"
#include "utils/base_64_coder.h"

using namespace NsProfileManager;

static const char * JSON_TAG_DATA = "data";
static const char * JSON_TAG_PROFILE_NAME = "profile";
static const char * JSON_TAG_APP_NAME = "application";


ProfilesAppsMessage::ProfilesAppsMessage(std::string const& serializedForm)
    : mData(NULL)
    , mDataSize(0)
{
    Json::Value root;
    Json::Reader reader;
    if (!reader.parse(serializedForm, root))
    {
        return;
    }
    if (!root.isObject())
    {
        return;
    }
    if (root.isMember(JSON_TAG_PROFILE_NAME) && root[JSON_TAG_PROFILE_NAME].isString())
    {
        mProfileName = root[JSON_TAG_PROFILE_NAME].asString();
    }
    if (root.isMember(JSON_TAG_APP_NAME) && root[JSON_TAG_APP_NAME].isString())
    {
        mApplicationName = root[JSON_TAG_APP_NAME].asString();
    }
    if (root.isMember(JSON_TAG_DATA) && root[JSON_TAG_DATA].isString())
    {
        std::string base64encoded = root[JSON_TAG_DATA].asString();
        int resultLength = 0;
        char * data = Base64Coder::decode(base64encoded.c_str(),
                                          base64encoded.size(), resultLength);
        mData = data;
        mDataSize = resultLength;
    }
}

ProfilesAppsMessage::ProfilesAppsMessage()
{
    mData = NULL;
    mDataSize = 0;
}

ProfilesAppsMessage::~ProfilesAppsMessage()
{
    if (mData != NULL)
    {
        delete mData;
    }
}

void ProfilesAppsMessage::setProfileName(std::string const& name)
{
    mProfileName = name;
}

void ProfilesAppsMessage::setApplicationName(std::string const& name)
{
    mApplicationName = name;
}

void ProfilesAppsMessage::setDataPayload(const char * data, const unsigned int size)
{
    mDataSize = size;
    if (mData != NULL)
    {
        delete mData;
    }
    mData = new char[size];
    memcpy(mData, data, size);
}

std::string ProfilesAppsMessage::getProfileName() const
{
    return mProfileName;
}

std::string ProfilesAppsMessage::getApplicationName() const
{
    return mApplicationName;
}

const char * ProfilesAppsMessage::getDataPayload() const
{
    return mData;
}

unsigned int ProfilesAppsMessage::getDataPayloadSize() const
{
    return mDataSize;
}

std::string ProfilesAppsMessage::serialize() const
{
    Json::Value root;
    int resultLength = 0;
    char * data = Base64Coder::encode(mData, mDataSize, resultLength);
    root[JSON_TAG_DATA] = std::string(data, resultLength);
    delete[] data;
    root[JSON_TAG_PROFILE_NAME] = mProfileName;
    root[JSON_TAG_APP_NAME] = mApplicationName;
    Json::FastWriter writer;
    return writer.write(root);
}

bool ProfilesAppsMessage::isComplete() const
{
    return mData != NULL && mDataSize != 0 && !mProfileName.empty() && !mApplicationName.empty();
}
