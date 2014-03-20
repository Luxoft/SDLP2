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

#ifndef SRC_COMPONENTS_APPLICATION_MANAGER_INCLUDE_APPLICATION_MANAGER_APPLICATION_H_
#define SRC_COMPONENTS_APPLICATION_MANAGER_INCLUDE_APPLICATION_MANAGER_APPLICATION_H_

#include <string>
#include <map>
#include "interfaces/MOBILE_API.h"
#include "connection_handler/device.h"

namespace NsSmartDeviceLink {
namespace NsSmartObjects {
class SmartObject;
}
}

namespace application_manager {

namespace mobile_api = mobile_apis;

namespace smart_objects = NsSmartDeviceLink::NsSmartObjects;
typedef int ErrorCode;

enum APIVersion {
  kUnknownAPI = -1,
  kAPIV1 = 1,
  kAPIV2 = 2
};

struct Version {
  APIVersion min_supported_api_version;
  APIVersion max_supported_api_version;

  Version()
    : min_supported_api_version(APIVersion::kUnknownAPI),
      max_supported_api_version(APIVersion::kUnknownAPI) {
  }
};

class InitialApplicationData {
  public:
    virtual ~InitialApplicationData() {
    }

    virtual const smart_objects::SmartObject* app_types() const = 0;
    virtual const smart_objects::SmartObject* vr_synonyms() const = 0;
    virtual const smart_objects::SmartObject* mobile_app_id() const = 0;
    virtual const smart_objects::SmartObject* tts_name() const = 0;
    virtual const smart_objects::SmartObject* ngn_media_screen_name() const = 0;
    virtual const mobile_api::Language::eType& language() const = 0;
    virtual const mobile_api::Language::eType& ui_language() const = 0;
    virtual void set_app_types(const smart_objects::SmartObject& app_types) = 0;
    virtual void set_vr_synonyms(
      const smart_objects::SmartObject& vr_synonyms) = 0;
    virtual void set_mobile_app_id(
      const smart_objects::SmartObject& mobile_app_id) = 0;
    virtual void set_tts_name(const smart_objects::SmartObject& tts_name) = 0;
    virtual void set_ngn_media_screen_name(
      const smart_objects::SmartObject& ngn_name) = 0;
    virtual void set_language(const mobile_api::Language::eType& language) = 0;
    virtual void set_ui_language(
      const mobile_api::Language::eType& ui_language) = 0;
};

/*
 * @brief Typedef for supported commands in application menu
 */
typedef std::map<unsigned int, smart_objects::SmartObject*> CommandsMap;

/*
 * @brief Typedef for supported sub menu in application menu
 */
typedef std::map<unsigned int, smart_objects::SmartObject*> SubMenuMap;

/*
 * @brief Typedef for interaction choice set
 */
typedef std::map<unsigned int, smart_objects::SmartObject*> ChoiceSetMap;

/*
 * @brief Typedef for perform interaction choice set
 */
typedef std::map<unsigned int, smart_objects::SmartObject*> PerformChoiceSetMap;

class DynamicApplicationData {
  public:
    virtual ~DynamicApplicationData() {
    }
    virtual const smart_objects::SmartObject* help_promt() const = 0;
    virtual const smart_objects::SmartObject* timeout_promt() const = 0;
    virtual const smart_objects::SmartObject* vr_help_title() const = 0;
    virtual const smart_objects::SmartObject* vr_help() const = 0;
    virtual const mobile_api::TBTState::eType& tbt_state() const = 0;
    virtual const smart_objects::SmartObject* show_command() const = 0;
    virtual const smart_objects::SmartObject* tbt_show_command() const = 0;

    virtual void set_help_prompt(
      const smart_objects::SmartObject& help_promt) = 0;
    virtual void set_timeout_prompt(
      const smart_objects::SmartObject& timeout_promt) = 0;
    virtual void set_vr_help_title(
      const smart_objects::SmartObject& vr_help_title) = 0;
    virtual void reset_vr_help_title() = 0;
    virtual void set_vr_help(const smart_objects::SmartObject& vr_help) = 0;
    virtual void reset_vr_help() = 0;
    virtual void set_tbt_state(const mobile_api::TBTState::eType& tbt_state) = 0;
    virtual void set_show_command(
      const smart_objects::SmartObject& show_command) = 0;
    virtual void set_tbt_show_command(
      const smart_objects::SmartObject& tbt_show) = 0;

    /*
     * @brief Adds a command to the in application menu
     */
    virtual void AddCommand(unsigned int cmd_id,
                            const smart_objects::SmartObject& command) = 0;

    /*
     * @brief Deletes all commands from the application
     * menu with the specified command id
     */
    virtual void RemoveCommand(unsigned int cmd_id) = 0;

    /*
     * @brief Finds command with the specified command id
     */
    virtual smart_objects::SmartObject* FindCommand(unsigned int cmd_id) = 0;

    /*
     * @brief Adds a menu to the application
     */
    virtual void AddSubMenu(unsigned int menu_id,
                            const smart_objects::SmartObject& menu) = 0;

    /*
     * @brief Deletes menu from the application menu
     */
    virtual void RemoveSubMenu(unsigned int menu_id) = 0;

    /*
     * @brief Finds menu with the specified id
     */
    virtual smart_objects::SmartObject* FindSubMenu(
      unsigned int menu_id) const = 0;

    /*
     * @brief Returns true if sub menu with such name already exist
     */
    virtual bool IsSubMenuNameAlreadyExist(const std::string& name) = 0;

    /*
     * @brief Adds a interaction choice set to the application
     *
     * @param choice_set_id Unique ID used for this interaction choice set
     * @param choice_set SmartObject that represent choice set
     */
    virtual void AddChoiceSet(unsigned int choice_set_id,
                              const smart_objects::SmartObject& choice_set) = 0;

    /*
     * @brief Deletes choice set from the application
     *
     * @param choice_set_id Unique ID of the interaction choice set
     */
    virtual void RemoveChoiceSet(unsigned int choice_set_id) = 0;

    /*
     * @brief Finds choice set with the specified choice_set_id id
     *
     * @param choice_set_id Unique ID of the interaction choice set
     */
    virtual smart_objects::SmartObject* FindChoiceSet(
      unsigned int choice_set_id) = 0;

    /*
     * @brief Adds perform interaction choice set to the application
     *
     * @param choice_set_id Unique ID used for this interaction choice set
     * @param choice_set SmartObject that represents choice set
     */
    virtual void AddPerformInteractionChoiceSet(
      unsigned int choice_set_id,
      const smart_objects::SmartObject& choice_set) = 0;

    /*
     * @brief Deletes entirely perform interaction choice set map
     *
     */
    virtual void DeletePerformInteractionChoiceSetMap() = 0;

    /*
     * @brief Retrieves entirely ChoiceSet - VR commands map
     *
     * @return ChoiceSet map that is currently in use
     */
    virtual const PerformChoiceSetMap&
    performinteraction_choice_set_map() const = 0;

    /*
     * @brief Retrieves choice set that is currently in use by perform
     * interaction
     *
     * @param choice_set_id Unique ID of the interaction choice set
     *
     * @return SmartObject that represents choice set
     */
    virtual smart_objects::SmartObject* FindPerformInteractionChoiceSet(
      unsigned int choice_set_id) const = 0;

    /*
     * @brief Retrieve application commands
     */
    virtual const CommandsMap& commands_map() const = 0;

    /*
     * @brief Retrieve application sub menus
     */
    virtual const SubMenuMap& sub_menu_map() const = 0;

    /*
     * @brief Retrieve application choice set map
     */
    virtual const ChoiceSetMap& choice_set_map() const = 0;

    /*
     * @brief Sets perform interaction state
     *
     * @param active Current state of the perform interaction
     */
    virtual void set_perform_interaction_active(unsigned int active) = 0;

    /*
     * @brief Retrieves perform interaction state
     *
     * @return TRUE if perform interaction active, otherwise FALSE
     */
    virtual unsigned int is_perform_interaction_active() const = 0;

    /*
     * @brief Sets the choice that was selected in
     * response to PerformInteraction
     *
     * @param choice Choice that was selected
     */
    virtual void set_perform_interaction_ui_corrid(unsigned int choice) = 0;

    /*
     * @brief Retrieve the choice that was selected in
     * response to PerformInteraction
     *
     * @return Choice that was selected in response to PerformInteraction
     */
    virtual unsigned int perform_interaction_ui_corrid() const = 0;

    /*
     * @brief Sets the mode for perform interaction: UI/VR/BOTH
     *
     * @param mode Mode that was selected (MENU; VR; BOTH)
     */
    virtual void set_perform_interaction_mode(int mode) = 0;

    /*
     * @brief Retrieve the mode that was PerformInteraction sent in
     *
     * @return mode of PerformInteraction
     */
    virtual inline int perform_interaction_mode() const = 0;

    /*
     * @brief Sets reset global properties state
     *
     * @param active Current state of the reset global properties
     */
    virtual void set_reset_global_properties_active(bool active) = 0;

    /*
     * @brief Retrieves reset global properties state
     *
     * @return TRUE if perform interaction active, otherwise FALSE
     */
    virtual bool is_reset_global_properties_active() const = 0;
};

class Application : public virtual InitialApplicationData,
  public virtual DynamicApplicationData {
  public:
    virtual ~Application() {
    }

    /**
     * @brief Returns message belonging to the application
     * that is currently executed (i.e. on HMI).
     * @return smart_objects::SmartObject * Active message
     */
    virtual const smart_objects::SmartObject* active_message() const = 0;

    virtual void CloseActiveMessage() = 0;
    virtual bool IsFullscreen() const = 0;
    virtual bool MakeFullscreen() = 0;
    virtual bool IsAudible() const = 0;
    virtual void MakeNotAudible() = 0;
    virtual bool allowed_support_navigation() const = 0;
    virtual void set_allowed_support_navigation(bool allow) = 0;
    virtual bool app_allowed() const = 0;
    virtual bool has_been_activated() const = 0;

    virtual const Version& version() const = 0;
    virtual unsigned int app_id() const = 0;
    virtual const std::string& name() const = 0;
    virtual bool is_media_application() const = 0;
    virtual const mobile_api::HMILevel::eType& hmi_level() const = 0;
    virtual const mobile_api::SystemContext::eType& system_context() const = 0;
    virtual const mobile_api::AudioStreamingState::eType&
    audio_streaming_state() const = 0;
    virtual const std::string& app_icon_path() const = 0;
    virtual connection_handler::DeviceHandle device() const = 0;

    virtual void set_version(const Version& version) = 0;
    virtual void set_name(const std::string& name) = 0;
    virtual void set_is_media_application(bool is_media) = 0;
    virtual void set_hmi_level(const mobile_api::HMILevel::eType& hmi_level) = 0;
    virtual void set_system_context(
      const mobile_api::SystemContext::eType& system_context) = 0;
    virtual void set_audio_streaming_state(
      const mobile_api::AudioStreamingState::eType& state) = 0;
    virtual bool set_app_icon_path(const std::string& file_name) = 0;
    virtual void set_app_allowed(const bool& allowed) = 0;
    virtual void set_device(connection_handler::DeviceHandle device) = 0;

    virtual bool AddFile(const std::string& file_name, bool is_persistent) = 0;
    virtual bool DeleteFile(const std::string& file_name) = 0;

    virtual bool SubscribeToButton(unsigned int btn_name) = 0;
    virtual bool IsSubscribedToButton(unsigned int btn_name) = 0;
    virtual bool UnsubscribeFromButton(unsigned int btn_name) = 0;

    virtual bool SubscribeToIVI(unsigned int vehicle_info_type_) = 0;
    virtual bool IsSubscribedToIVI(unsigned int vehicle_info_type_) = 0;
    virtual bool UnsubscribeFromIVI(unsigned int vehicle_info_type_) = 0;
};

}  // namespace application_manager

#endif  // SRC_COMPONENTS_APPLICATION_MANAGER_INCLUDE_APPLICATION_MANAGER_APPLICATION_H_
