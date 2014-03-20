/**
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

#ifndef SRC_COMPONENTS_APPLICATION_MANAGER_INCLUDE_APPLICATION_MANAGER_MESSAGE_CHAINING_IMPL_H_
#define SRC_COMPONENTS_APPLICATION_MANAGER_INCLUDE_APPLICATION_MANAGER_MESSAGE_CHAINING_IMPL_H_

#include "smart_objects/smart_object.h"
#include "interfaces/HMI_API.h"

namespace application_manager {

namespace smart_objects = NsSmartDeviceLink::NsSmartObjects;

/**
 * @brief Messagechaining class
 *
 * Class used to store temporary Mobile request data, connection key,
 * because of HMI response doesn't contain it, but it is required for
 * Mobile response and correlation Id.
 * Class provides also possibility to store intermediate HMI response
 * results required by Mobile response.
 */
class MessageChaining {
 public:
  /**
   * @brief MessageChaining class constructor
   *
   * @param connection_key of connection for Mobile side
   * @param correlation_id Correlation id for response for Mobile side
   */
  MessageChaining(unsigned int connection_key, unsigned int correlation_id);
  /**
   * @brief MessageChaining class destructor
   */
  ~MessageChaining();

  /**
   * @brief Comparison operator.
   *
   * @param other Reference to the object to be compared with
   * @return bool
   */
  bool operator==(const MessageChaining& other) const;

  /**
   * @brief Retrieves correlation ID
   *
   * @return correlation_id
   */
  const unsigned int correlation_id() const;

  /**
   * @brief Retrieves connection key
   *
   * @return connection_key
   */
  const unsigned int connection_key() const;

  /**
   * @brief Increments counter that represent
   * amount of pending HMI responses
   *
   */
  void IncrementCounter();

  /**
   * @brief Decrements counter representing that
   * corresponding HMI response received
   *
   */
  void DecrementCounter();

  /**
   * @brief Retrieves counter
   *
   * @return counter
   */
  int counter() const;

  /**
   * @brief Sets counter
   *
   * @param counter Param indicates how many responses expected
   */
  void set_counter(const unsigned int& counter);

  /**
   * @brief Sets SmartObject data
   *
   * @return reference to SmartObject data
   */
  void set_data(const smart_objects::SmartObject& data);

  /**
   * @brief Retrieves SmartObject data
   *
   * @return reference to SmartObject data
   */
  const smart_objects::SmartObject& data() const;

  /**
   * @brief Sets VR HMI response result code
   *
   * @param result Received VR HMI result code
   */
  void set_vr_response_result(const hmi_apis::Common_Result::eType& result);

  /**
   * @brief Retrieve HMI VR response result code
   */
  const hmi_apis::Common_Result::eType& vr_response_result() const;

  /**
   * @brief Sets UI HMI response result code
   *
   * @param result Received UI HMI result code
   */
  void set_ui_response_result(const hmi_apis::Common_Result::eType& result);

  /**
   * @brief Retrieve HMI UI response result code
   */
  const hmi_apis::Common_Result::eType& ui_response_result() const;

  /**
   * @brief Sets TTS HMI response result code
   *
   * @param result Received UI HMI result code
   */
  void set_tts_response_result(const hmi_apis::Common_Result::eType& result);

  /**
   * @brief Retrieve HMI TTS response result code
   */
  const hmi_apis::Common_Result::eType& tts_response_result() const;

 private:
  unsigned int correlation_id_;
  unsigned int connection_key_;
  bool success_;
  int counter_;  // amount of pending HMI responses
  smart_objects::SmartObject data_;   // temporal data
  hmi_apis::Common_Result::eType vr_response_result_;
  hmi_apis::Common_Result::eType ui_response_result_;
  hmi_apis::Common_Result::eType tts_response_result_;
  DISALLOW_COPY_AND_ASSIGN(MessageChaining);
};

}  // namespace application_manager

#endif  // SRC_COMPONENTS_APPLICATION_MANAGER_INCLUDE_APPLICATION_MANAGER_MESSAGE_CHAINING_IMPL_H_
