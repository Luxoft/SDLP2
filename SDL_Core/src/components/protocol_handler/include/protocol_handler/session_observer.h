/**
 * \file ISessionObserver.h
 * \brief ISessionObserver class header file.
 *
 * Copyright (c) 2013, Ford Motor Company
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following
 * disclaimer in the documentation and/or other materials provided with the
 * distribution.
 *
 * Neither the name of the Ford Motor Company nor the names of its contributors
 * may be used to endorse or promote products derived from this software
 * without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

#ifndef SRC_COMPONENTS_PROTOCOL_HANDLER_INCLUDE_PROTOCOL_HANDLER_SESSION_OBSERVER_H_
#define SRC_COMPONENTS_PROTOCOL_HANDLER_INCLUDE_PROTOCOL_HANDLER_SESSION_OBSERVER_H_

#include <list>
#include <string>
#include "transport_manager/transport_manager.h"
#include "connection_handler/connection_handler.h"

/**
 *\namespace NsProtocolHandler
 *\brief Namespace for SmartDeviceLink ProtocolHandler related functionality.
 */
namespace protocol_handler {
/**
 * \class SessionObserver
 * \brief Interface for making a bridge between ProtocolHandler and
 * ConnectionHandler components.
 */
class SessionObserver {
  public:
    /**
     * \brief Callback function used by ProtocolHandler
     * when Mobile Application initiates start of new session.
     * \param connection_handle Connection identifier whithin which session
     * has to be started.
     * \return int Id (number) of new session if successful otherwise -1.
     */
    virtual unsigned int OnSessionStartedCallback(
      transport_manager::ConnectionUID connection_handle) = 0;

    /**
     * \brief Callback function used by ProtocolHandler
     * when Mobile Application initiates session ending.
     * \param connection_handle Connection identifier whithin which session exists
     * \param sessionId Identifier of the session to be ended
     * \param hashCode Hash used only in second version of SmartDeviceLink protocol.
     * If not equal to hash assigned to session on start then operation fails.
     * \return unsigned int 0 if operation fails session key otherwise
     */
    virtual unsigned int OnSessionEndedCallback(
      transport_manager::ConnectionUID connection_handle,
      unsigned char sessionId,
      unsigned int hashCode) = 0;

    /**
     * \brief Creates unique identifier of session (can be used as hash)
     * from given connection identifier
     * whithin which session exists and session number.
     * \param  connection_handle Connection identifier whithin which session exists
     * \param sessionId Identifier of the session
     * \return unsigned int Unique key for session
     */
    virtual unsigned int KeyFromPair(
      transport_manager::ConnectionUID connection_handle,
      unsigned char sessionId) = 0;

    /**
     * \brief Returns connection identifier and session number from given
     * session key
     * \param key Unique key used by other components as session identifier
     * \param connection_handle Returned: Connection identifier whithin which
     * session exists
     * \param sessionId Returned: Number of session
     */
    virtual void PairFromKey(
      unsigned int key,
      transport_manager::ConnectionUID* connection_handle,
      unsigned char* sessionId) = 0;

    /**
     * \brief information about given Connection Key.
     * \param key Unique key used by other components as session identifier
     * \param app_id Returned: ApplicationID
     * \param sessions_list Returned: List of session keys
     * \param device_id Returned: DeviceID
     * \return int -1 in case of error or 0 in case of success
     */
    virtual int GetDataOnSessionKey(unsigned int key,
                                    unsigned int* app_id,
                                    std::list<int>* sessions_list,
                                    unsigned int* device_id) = 0;

    /**
     * \brief information about given Connection Key.
     * \param key Unique key used by other components as session identifier
     * \param app_id Returned: ApplicationID
     * \param sessions_list Returned: List of session keys
     * \param device_id Returned: DeviceID
     * \return int -1 in case of error or 0 in case of success
     */
    virtual int GetDataOnDeviceID(
        unsigned int device_handle,
      std::string* device_name,
      std::list<unsigned int>* applications_list) = 0;

  protected:
    /**
     * \brief Destructor
     */
    virtual ~SessionObserver() {
    }
};
}  // namespace protocol_handler

#endif  // SRC_COMPONENTS_PROTOCOL_HANDLER_INCLUDE_PROTOCOL_HANDLER_SESSION_OBSERVER_H_
