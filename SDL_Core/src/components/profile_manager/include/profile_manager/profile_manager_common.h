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


#ifndef PROFILEMANAGERCOMMON_H
#define PROFILEMANAGERCOMMON_H

#define PROFILE_MANAGER_TO_PROFILE_INITIAL_ID 100
#define PROFILE_TO_PROFILE_MANAGER_INITIAL_ID 200

#include <unistd.h>
#include "profileApi/MobileAppState.h"

/**
  *\namespace NsProfileManager
  *\brief Namespace for SmartDeviceLink ProfileManager related functionality.
*/
namespace NsProfileManager
{

static const char * CREATE_PROFILE_FUNC_NAME = "CreateProfile";
static const char * DESTROY_PROFILE_FUNC_NAME = "DestroyProfile";
static const char * PROFILE_PROCESS_NAME = "ProfileProcess";

static const char * PROFILE_MAN_REPLY_SOCKET = "ProfileManagerReply";
// Reply for 0MQ messages
static const char * MSG_REPLY_OK = "OK";



/**
 * @brief The MessageTypes enum provides the message types for exchange between
 * ProfileINstanceManager and ProfileProcess
 */
enum MessageTypes
{
    MSG_TYPE_INVALID = 0,
    // messages from PM to profile process
    MSG_TYPE_LOAD_PROFILE = PROFILE_MANAGER_TO_PROFILE_INITIAL_ID,
    MSG_TYPE_UNLOAD_PROFILE,
    MSG_TYPE_APP_TO_PROFILE_MESSAGE,
    MSG_TYPE_APP_STATE_CHANGED,
    MSG_TYPE_MOBILE_APP_EXITED,
    MSG_TYPE_HMI_NOTIFICATION_IVILINK,

    // messages from profile process to PM
    MSG_TYPE_PROFILE_TO_APP_MESSAGE = PROFILE_TO_PROFILE_MANAGER_INITIAL_ID,
    MSG_TYPE_PROFILE_TO_APP_MESSAGE_BROADCAST,
    MSG_TYPE_HMI_REQUEST_IVILINK,
};

/**
 * @brief The ZmqMessageHeader struct is the header of the message for
 *        ProfileINstanceManager <-> ProfileProcess exchange
 */
struct ZmqMessageHeader
{
    ZmqMessageHeader()
    {
        appId = 0;
        state = 0;
        messageType = MSG_TYPE_INVALID;
        dataSize = 0;
        pid = getpid();
        protocolVersion = 0;
    }

    /**
     * @brief appId Application id
     */
    int appId;

    /**
     * @brief state ProfileState or MobileAppState
     */
    int state;

    /**
     * @brief messageType Message type
     */
    MessageTypes messageType;

    pid_t pid;

    /**
     * @brief dataSize Payload data size (data field from ZmqMessage struct)
     */
    int dataSize;

    /**
     * \brief Version of HMI RPC message protocol
     */
    unsigned char protocolVersion;
} __attribute__((packed));

} //NsProfileManager

#endif //PROFILEMANAGERCOMMON_H
