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


#ifndef __PROFILES_APPS_MESSAGE_H__
#define __PROFILES_APPS_MESSAGE_H__

namespace NsProfileManager
{
/**
 * Message that an application may use to send data to Profile
 * via Application Manager HMI (set Action to FROM_APP_TO_PROFILE
 * and set Info as this message in serialized form (see ApplicationManager::JsonMessage)
 *
 * Messages from profile will arrive to app in a JsonMessage with Message = PROFILE_DATA
 * and getInfo will return a ProfilesAppsMessage in serialized form.
 */
class ProfilesAppsMessage
{
public:
    ProfilesAppsMessage(std::string const& serializedForm);

    ProfilesAppsMessage();
    ~ProfilesAppsMessage();

    void setProfileName(std::string const& name);
    void setApplicationName(std::string const& name);
    void setDataPayload(const char * data, const unsigned int size);

    std::string getProfileName() const;
    std::string getApplicationName() const;
    const char * getDataPayload() const;
    unsigned int getDataPayloadSize() const;

    std::string serialize() const;

    bool isComplete() const;
private:
    char * mData;
    unsigned int mDataSize;
    std::string mProfileName;
    std::string mApplicationName;
};
}

#endif //__PROFILES_APPS_MESSAGE_H__
