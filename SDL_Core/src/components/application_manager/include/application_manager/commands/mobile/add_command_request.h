/*

 Copyright (c) 2013, Ford Motor Company
 All rights reserved.

 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions are met:

 Redistributions of source code must retain the above copyright notice, this
 list of conditions and the following disclaimer.

 Redistributions in binary form must reproduce the above copyright notice,
 this list of conditions and the following
 disclaimer in the documentation and/or other materials provided with the
 distribution.

 Neither the name of the Ford Motor Company nor the names of its contributors
 may be used to endorse or promote products derived from this software
 without specific prior written permission.

 THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 POSSIBILITY OF SUCH DAMAGE.
 */

#ifndef SRC_COMPONENTS_APPLICATION_MANAGER_INCLUDE_APPLICATION_MANAGER_COMMANDS_ADD_COMMAND_REQUEST_H_
#define SRC_COMPONENTS_APPLICATION_MANAGER_INCLUDE_APPLICATION_MANAGER_COMMANDS_ADD_COMMAND_REQUEST_H_

#include "application_manager/commands/command_request_impl.h"
#include "utils/macro.h"

namespace application_manager {

class Application;

namespace commands {

/**
 * @brief AddCommandRequest command class
 **/
class AddCommandRequest : public CommandRequestImpl {
 public:
  /**
   * @brief AddCommandRequest class constructor
   *
   * @param message Incoming SmartObject message
   **/
  explicit AddCommandRequest(const MessageSharedPtr& message);

  /**
   * @brief AddCommandRequest class destructor
   **/
  virtual ~AddCommandRequest();

  /**
   * @brief Execute command
   **/
  virtual void Run();

 private:

  /*
   * @brief Check if command name doesn't exist in application
   * Please see SDLAQ-CRS-407 for more information
   *
   * @param app Mobile application
   *
   * @return TRUE on success, otherwise FALSE
   */
  bool CheckCommandName(const Application* app);

  /*
   * @brief Check if command VR synonyms doesn't exist in application commands
   * Please see SDLAQ-CRS-407 for more information
   *
   * @param app Mobile application
   *
   * @return TRUE on success, otherwise FALSE
   */
  bool CheckCommandVRSynonym(const Application* app);

  /*
   * @brief Check if command parent ID exists in submenu map
   *
   * @param app Mobile application
   *
   * @return TRUE on success, otherwise FALSE
   */
  bool CheckCommandParentId(const Application* app);

  bool CheckVRCommandsNames();

  DISALLOW_COPY_AND_ASSIGN(AddCommandRequest);
};

}  // namespace commands
}  // namespace application_manager

#endif  // SRC_COMPONENTS_APPLICATION_MANAGER_INCLUDE_APPLICATION_MANAGER_COMMANDS_ADD_COMMAND_REQUEST_H_
