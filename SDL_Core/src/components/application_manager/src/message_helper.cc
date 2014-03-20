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

#include <set>
#include <string>
#include "application_manager/application_manager_impl.h"
#include "application_manager/commands/command_impl.h"
#include "application_manager/smart_object_keys.h"
#include "application_manager/message_helper.h"
#include "config_profile/profile.h"
#include "interfaces/HMI_API.h"
#include "interfaces/MOBILE_API.h"
#include "utils/file_system.h"

namespace application_manager {

const VehicleData MessageHelper::vehicle_data_ = { {
    strings::gps,
    VehicleDataType::GPS
  }, { strings::speed, VehicleDataType::SPEED }, {
    strings::rpm, VehicleDataType::RPM
  }, {
    strings::fuel_level,
    VehicleDataType::FUELLEVEL
  }, {
    strings::fuel_level_state,
    VehicleDataType::FUELLEVEL_STATE
  }, {
    strings::instant_fuel_consumption,
    VehicleDataType::FUELCONSUMPTION
  }, {
    strings::external_temp,
    VehicleDataType::EXTERNTEMP
  }, { strings::vin, VehicleDataType::VIN }, {
    strings::prndl, VehicleDataType::PRNDL
  }, {
    strings::tire_pressure,
    VehicleDataType::TIREPRESSURE
  }, {
    strings::odometer,
    VehicleDataType::ODOMETER
  }, {
    strings::belt_status,
    VehicleDataType::BELTSTATUS
  }, {
    strings::body_information,
    VehicleDataType::BODYINFO
  }, {
    strings::device_status,
    VehicleDataType::DEVICESTATUS
  }, {
    strings::e_call_info,
    VehicleDataType::ECALLINFO
  }, {
    strings::airbag_status,
    VehicleDataType::AIRBAGSTATUS
  }, {
    strings::emergency_event,
    VehicleDataType::EMERGENCYEVENT
  }, {
    strings::cluster_mode_status,
    VehicleDataType::CLUSTERMODESTATUS
  }, {
    strings::my_key,
    VehicleDataType::MYKEY
  }, {
    strings::driver_braking,
    VehicleDataType::BRAKING
  }, {
    strings::wiper_status,
    VehicleDataType::WIPERSTATUS
  }, {
    strings::head_lamp_status,
    VehicleDataType::HEADLAMPSTATUS
  },
  /*
   NOT DEFINED in mobile API
   {strings::gps,                      VehicleDataType::BATTVOLTAGE},
   */
  { strings::engine_torque, VehicleDataType::ENGINETORQUE }, {
    strings::acc_pedal_pos, VehicleDataType::ACCPEDAL
  }, {
    strings::steering_wheel_angle, VehicleDataType::STEERINGWHEEL
  },
};

void MessageHelper::SendHMIStatusNotification(
  const Application& application_impl) {
  smart_objects::SmartObject* notification = new smart_objects::SmartObject;
  if (!notification) {
    // TODO(VS): please add logger.
    return;
  }
  smart_objects::SmartObject& message = *notification;

  message[strings::params][strings::function_id] =
    mobile_api::FunctionID::OnHMIStatusID;

  message[strings::params][strings::message_type] = MessageType::kNotification;

  message[strings::params][strings::connection_key] = application_impl.app_id();

  message[strings::msg_params][strings::hmi_level] =
    application_impl.hmi_level();

  message[strings::msg_params][strings::audio_streaming_state] =
    application_impl.audio_streaming_state();

  message[strings::msg_params][strings::system_context] = application_impl
      .system_context();

  ApplicationManagerImpl::instance()->ManageMobileCommand(notification);
}

void MessageHelper::SendOnAppRegisteredNotificationToHMI(
  const Application& application_impl) {
  smart_objects::SmartObject* notification = new smart_objects::SmartObject;
  if (!notification) {
    // TODO(VS): please add logger.
    return;
  }
  smart_objects::SmartObject& message = *notification;

  message[strings::params][strings::function_id] =
    hmi_apis::FunctionID::BasicCommunication_OnAppRegistered;

  message[strings::params][strings::message_type] = MessageType::kNotification;

  message[strings::msg_params][strings::application][strings::app_name] =
    application_impl.name();

  const smart_objects::SmartObject* ngn_media_screen_name =
    application_impl.ngn_media_screen_name();

  if (ngn_media_screen_name) {
    message[strings::msg_params][strings::application]
    [strings::ngn_media_screen_app_name] = *ngn_media_screen_name;
  }

  message[strings::msg_params][strings::application][strings::icon] =
    application_impl.app_icon_path();

  std::string dev_name = ApplicationManagerImpl::instance()->GetDeviceName(
                           application_impl.device());
  message[strings::msg_params][strings::application][strings::device_name] =
    dev_name;

  message[strings::msg_params][strings::application][strings::app_id] =
    application_impl.app_id();

  message[strings::msg_params][strings::application][strings::hmi_display_language_desired] =
    application_impl.ui_language();

  message[strings::msg_params][strings::application][strings::is_media_application] =
    application_impl.is_media_application();

  const smart_objects::SmartObject* app_type = application_impl.app_types();

  if (app_type) {
    message[strings::msg_params][strings::application][strings::app_type] =
      *app_type;
  }

  ApplicationManagerImpl::instance()->ManageHMICommand(notification);
}

smart_objects::SmartObject* MessageHelper::CreateGeneralVrCommand() {
  smart_objects::SmartObject* vr_help_command = new smart_objects::SmartObject(
    smart_objects::SmartType_Array);
  if (!vr_help_command) {
    return NULL;
  }
  smart_objects::SmartObject& help_object = *vr_help_command;
  const std::vector<std::string>& vr_general_cmds = profile::Profile::instance()
      ->vr_commands();
  for (int i = 0; i < vr_general_cmds.size(); ++i) {
    help_object[i] = vr_general_cmds[i];
  }
  return vr_help_command;
}

void MessageHelper::SendHelpVrCommand() {
  smart_objects::SmartObject* vr_help_command = CreateGeneralVrCommand();
  if (!vr_help_command) {
    return;
  }
  unsigned int max_cmd_id = profile::Profile::instance()->max_cmd_id();
  SendAddVRCommandToHMI(max_cmd_id + 1, *vr_help_command, 0);
}

void MessageHelper::SendVrCommandsOnRegisterAppToHMI(Application* app) {
  unsigned int max_cmd_id = profile::Profile::instance()->max_cmd_id();

  if (app->vr_synonyms()) {
    SendAddVRCommandToHMI(max_cmd_id + app->app_id(), *app->vr_synonyms(),
                          app->app_id());
  }
}

void MessageHelper::SendRemoveVrCommandsOnUnregisterApp(Application* app) {
  unsigned int max_cmd_id = profile::Profile::instance()->max_cmd_id();

  if (app->vr_synonyms()) {
    SendRemoveCommandToHMI(hmi_apis::FunctionID::VR_DeleteCommand,
                           max_cmd_id + app->app_id(), app->app_id());
  }
}

void MessageHelper::SendOnAppInterfaceUnregisteredNotificationToMobile(
  int connection_key,
  mobile_api::AppInterfaceUnregisteredReason::eType reason) {
  smart_objects::SmartObject* notification = new smart_objects::SmartObject;
  if (!notification) {
    // TODO(VS): please add logger.
    return;
  }
  smart_objects::SmartObject& message = *notification;

  message[strings::params][strings::function_id] =
    mobile_api::FunctionID::OnAppInterfaceUnregisteredID;

  message[strings::params][strings::message_type] = MessageType::kNotification;

  message[strings::params][strings::connection_key] = connection_key;

  message[strings::msg_params][strings::reason] = reason;

  ApplicationManagerImpl::instance()->ManageMobileCommand(notification);
}

const VehicleData& MessageHelper::vehicle_data() {
  return vehicle_data_;
}

smart_objects::SmartObject* MessageHelper::CreateBlockedByPoliciesResponse(
  mobile_apis::FunctionID::eType function_id,
  mobile_apis::Result::eType result, unsigned int correlation_id,
  unsigned int connection_key) {
  smart_objects::SmartObject* response = new smart_objects::SmartObject;
  if (!response) {
    return NULL;
  }

  (*response)[strings::params][strings::function_id] = function_id;
  (*response)[strings::params][strings::message_type] = MessageType::kResponse;
  (*response)[strings::msg_params][strings::success] = false;
  (*response)[strings::msg_params][strings::result_code] = result;
  (*response)[strings::params][strings::correlation_id] = correlation_id;
  (*response)[strings::params][strings::connection_key] = connection_key;
  (*response)[strings::params][strings::protocol_type] =
    commands::CommandImpl::mobile_protocol_type_;
  (*response)[strings::params][strings::protocol_version] =
    ProtocolVersion::kV2;

  return response;
}

smart_objects::SmartObject* MessageHelper::CreateDeviceListSO(
  const connection_handler::DeviceList& devices) {
  smart_objects::SmartObject* device_list_so = new smart_objects::SmartObject(
    smart_objects::SmartType_Map);

  if (NULL == device_list_so) {
    return NULL;
  }

  (*device_list_so)[strings::device_list] = smart_objects::SmartObject(
        smart_objects::SmartType_Array);
  smart_objects::SmartObject& list_so = (*device_list_so)[strings::device_list];
  int index = 0;
  for (connection_handler::DeviceList::const_iterator it = devices.begin();
       devices.end() != it; ++it) {
    list_so[index][strings::name] = it->second.user_friendly_name();
    list_so[index][strings::id] = it->second.device_handle();
    ++index;
  }
  return device_list_so;
}

smart_objects::SmartObject* MessageHelper::CreateModuleInfoSO(
  unsigned int function_id) {
  smart_objects::SmartObject* module_info = new smart_objects::SmartObject(
    smart_objects::SmartType_Map);
  if (NULL == module_info) {
    return NULL;
  }
  smart_objects::SmartObject& object = *module_info;
  object[strings::params][strings::message_type] = MessageType::kRequest;
  object[strings::params][strings::function_id] = function_id;
  object[strings::params][strings::correlation_id] =
    ApplicationManagerImpl::instance()->GetNextHMICorrelationID();
  object[strings::msg_params] = smart_objects::SmartObject(
                                  smart_objects::SmartType_Map);
  return module_info;
}

smart_objects::SmartObject* MessageHelper::CreateSetAppIcon(
  const std::string& path_to_icon, unsigned int app_id) {
  smart_objects::SmartObject* set_icon = new smart_objects::SmartObject(
    smart_objects::SmartType_Map);

  if (!set_icon) {
    return NULL;
  }

  smart_objects::SmartObject& object = *set_icon;
  object[strings::sync_file_name][strings::value] = path_to_icon;
  // TODO(PV): need to store actual image type
  object[strings::sync_file_name][strings::image_type] =
    mobile_api::ImageType::DYNAMIC;
  object[strings::app_id] = app_id;

  return set_icon;
}

void MessageHelper::SendAppDataToHMI(const Application* app) {
  unsigned int id = app->app_id();

  utils::SharedPtr<smart_objects::SmartObject> set_app_icon(
    new smart_objects::SmartObject);
  if (set_app_icon) {
    smart_objects::SmartObject& so_to_send = *set_app_icon;
    so_to_send[strings::params][strings::function_id] =
      hmi_apis::FunctionID::UI_SetAppIcon;
    so_to_send[strings::params][strings::message_type] =
      hmi_apis::messageType::request;
    so_to_send[strings::params][strings::protocol_version] =
      commands::CommandImpl::protocol_version_;
    so_to_send[strings::params][strings::protocol_type] =
      commands::CommandImpl::hmi_protocol_type_;
    so_to_send[strings::params][strings::correlation_id] =
      ApplicationManagerImpl::instance()->GetNextHMICorrelationID();

    so_to_send[strings::msg_params] = smart_objects::SmartObject(
                                        smart_objects::SmartType_Map);
    smart_objects::SmartObject* msg_params = MessageHelper::CreateSetAppIcon(
          app->app_icon_path(), id);

    if (msg_params) {
      so_to_send[strings::msg_params] = *msg_params;
    }
    // TODO(PV): appropriate handling of result
    ApplicationManagerImpl::instance()->ManageHMICommand(set_app_icon);
  }

  SendGlobalPropertiesToHMI(app);
  SendShowRequestToHMI(app);
  SendAddCommandRequestToHMI(app);
  SendChangeRegistrationRequestToHMI(app);
}

void MessageHelper::SendGlobalPropertiesToHMI(const Application* app) {
  if (!app) {
    return;
  }

  // UI global properties
  if (app->vr_help_title() || app->vr_help()) {
    smart_objects::SmartObject* ui_global_properties =
      new smart_objects::SmartObject(smart_objects::SmartType_Map);

    if (!ui_global_properties) {
      return;
    }

    (*ui_global_properties)[strings::params][strings::function_id] =
      hmi_apis::FunctionID::UI_SetGlobalProperties;
    (*ui_global_properties)[strings::params][strings::message_type] =
      hmi_apis::messageType::request;
    (*ui_global_properties)[strings::params][strings::protocol_version] =
      commands::CommandImpl::protocol_version_;
    (*ui_global_properties)[strings::params][strings::protocol_type] =
      commands::CommandImpl::hmi_protocol_type_;
    (*ui_global_properties)[strings::params][strings::correlation_id] =
      ApplicationManagerImpl::instance()->GetNextHMICorrelationID();

    smart_objects::SmartObject ui_msg_params = smart_objects::SmartObject(
          smart_objects::SmartType_Map);
    if (app->vr_help_title()) {
      ui_msg_params[strings::vr_help_title] = (*app->vr_help_title());
    }
    if (app->vr_help()) {
      ui_msg_params[strings::vr_help] = (*app->vr_help());
    }
    ui_msg_params[strings::app_id] = app->app_id();

    (*ui_global_properties)[strings::msg_params] = ui_msg_params;

    ApplicationManagerImpl::instance()->ManageHMICommand(ui_global_properties);
  }

  // TTS global properties
  if (app->help_promt() || app->timeout_promt()) {
    smart_objects::SmartObject* tts_global_properties =
      new smart_objects::SmartObject(smart_objects::SmartType_Map);

    if (!tts_global_properties) {
      return;
    }

    (*tts_global_properties)[strings::params][strings::function_id] =
      hmi_apis::FunctionID::TTS_SetGlobalProperties;
    (*tts_global_properties)[strings::params][strings::message_type] =
      hmi_apis::messageType::request;
    (*tts_global_properties)[strings::params][strings::protocol_version] =
      commands::CommandImpl::protocol_version_;
    (*tts_global_properties)[strings::params][strings::protocol_type] =
      commands::CommandImpl::hmi_protocol_type_;
    (*tts_global_properties)[strings::params][strings::correlation_id] =
      ApplicationManagerImpl::instance()->GetNextHMICorrelationID();

    smart_objects::SmartObject tts_msg_params = smart_objects::SmartObject(
          smart_objects::SmartType_Map);
    if (app->help_promt()) {
      tts_msg_params[strings::help_prompt] = (*app->help_promt());
    }
    if (app->timeout_promt()) {
      tts_msg_params[strings::timeout_prompt] = (*app->timeout_promt());
    }
    tts_msg_params[strings::app_id] = app->app_id();

    (*tts_global_properties)[strings::msg_params] = tts_msg_params;

    ApplicationManagerImpl::instance()->ManageHMICommand(tts_global_properties);
  }
}

smart_objects::SmartObject* MessageHelper::CreateAppVrHelp(const Application* app) {
  smart_objects::SmartObject* result = new smart_objects::SmartObject(
    smart_objects::SmartType_Map);
  if (!result) {
    return NULL;
  }
  smart_objects::SmartObject& vr_help = *result;
  if (app->vr_help_title()) {
    vr_help[strings::vr_help_title] = (*app->vr_help_title());
  } else {
    vr_help[strings::vr_help_title] = app->name();
  }

  if (app->vr_help()) {
    vr_help[strings::vr_help] = (*app->vr_help());
  } else {
    const std::set<Application*>& apps = ApplicationManagerImpl::instance()
                                         ->applications();

    int index = 0;
    std::set<Application*>::const_iterator it_app = apps.begin();
    for (; apps.end() != it_app; ++it_app) {
      if ((*it_app)->vr_synonyms()) {
        smart_objects::SmartObject item(smart_objects::SmartType_Map);
        item[strings::text] = (*((*it_app)->vr_synonyms())).getElement(0);
        item[strings::position] = index;
        vr_help[strings::vr_help][index++] = item;
      }
    }

    // copy all app VR commands
    const CommandsMap& commands = app->commands_map();
    CommandsMap::const_iterator it = commands.begin();

    for (; commands.end() != it; ++it) {
      smart_objects::SmartObject item(smart_objects::SmartType_Map);
      item[strings::text] =
        (*it->second)[strings::vr_commands][0].asString();
      item[strings::position] = index;
      vr_help[strings::vr_help][index++] = item;
    }
  }
  return result;
}

void MessageHelper::SendShowVrHelpToHMI(const Application* app) {
  smart_objects::SmartObject* show_vr_help = new smart_objects::SmartObject(
    smart_objects::SmartType_Map);

  if (!show_vr_help) {
    return;
  }

  smart_objects::SmartObject ui_msg_params = smart_objects::SmartObject(
        smart_objects::SmartType_Map);

  if (!app) {
    ui_msg_params[strings::vr_help_title] = profile::Profile::instance()
                                            ->vr_help_title();

    smart_objects::SmartObject* vr_commands_array = CreateGeneralVrCommand();
    if (!vr_commands_array) {
      return;
    }

    ui_msg_params[strings::vr_help] = smart_objects::SmartObject(
                                        smart_objects::SmartType_Array);

    int help_size = vr_commands_array->length();
    for (int i = 0; i < help_size; ++i) {
      smart_objects::SmartObject item(smart_objects::SmartType_Map);
      item[strings::text] = (*vr_commands_array).getElement(i);
      item[strings::position] = i;

      ui_msg_params[strings::vr_help][i++] = item;
    }

    const std::set<Application*>& apps = ApplicationManagerImpl::instance()
                                         ->applications();

    int i = help_size;
    std::set<Application*>::const_iterator it = apps.begin();
    for (; apps.end() != it; ++it) {
      if ((*it)->vr_synonyms()) {
        smart_objects::SmartObject item(smart_objects::SmartType_Map);
        item[strings::text] = (*((*it)->vr_synonyms())).getElement(0);
        item[strings::position] = i;
        ui_msg_params[strings::vr_help][i++] = item;
      }
    }
  } else {
    smart_objects::SmartObject* vr_help = CreateAppVrHelp(app);
    if (!vr_help) {
      return;
    }
    ui_msg_params = *vr_help;
    ui_msg_params[strings::app_id] = app->app_id();
  }

  (*show_vr_help)[strings::params][strings::function_id] =
    hmi_apis::FunctionID::UI_ShowVrHelp;
  (*show_vr_help)[strings::params][strings::message_type] =
    hmi_apis::messageType::request;
  (*show_vr_help)[strings::params][strings::protocol_version] =
    commands::CommandImpl::protocol_version_;
  (*show_vr_help)[strings::params][strings::protocol_type] =
    commands::CommandImpl::hmi_protocol_type_;
  (*show_vr_help)[strings::params][strings::correlation_id] =
    ApplicationManagerImpl::instance()->GetNextHMICorrelationID();
  (*show_vr_help)[strings::msg_params] = ui_msg_params;

  ApplicationManagerImpl::instance()->ManageHMICommand(show_vr_help);
}

void MessageHelper::SendShowRequestToHMI(const Application* app) {
  if (!app) {
    return;
  }

  smart_objects::SmartObject* ui_show = new smart_objects::SmartObject(
    smart_objects::SmartType_Map);

  if (!ui_show) {
    return;
  }

  if (app->show_command()) {
    (*ui_show)[strings::params][strings::function_id] =
      hmi_apis::FunctionID::UI_Show;
    (*ui_show)[strings::params][strings::message_type] =
      hmi_apis::messageType::request;
    (*ui_show)[strings::params][strings::protocol_version] =
      commands::CommandImpl::protocol_version_;
    (*ui_show)[strings::params][strings::protocol_type] =
      commands::CommandImpl::hmi_protocol_type_;
    (*ui_show)[strings::params][strings::correlation_id] =
      ApplicationManagerImpl::instance()->GetNextHMICorrelationID();
    (*ui_show)[strings::msg_params] = (*app->show_command());
    ApplicationManagerImpl::instance()->ManageHMICommand(ui_show);
  }
}

void MessageHelper::SendShowConstantTBTRequestToHMI(const Application* app) {
  if (!app) {
    return;
  }

  smart_objects::SmartObject* navi_show_tbt = new smart_objects::SmartObject(
    smart_objects::SmartType_Map);

  if (!navi_show_tbt) {
    return;
  }

  if (app->tbt_show_command()) {
    (*navi_show_tbt)[strings::params][strings::function_id] =
      hmi_apis::FunctionID::Navigation_ShowConstantTBT;
    (*navi_show_tbt)[strings::params][strings::message_type] =
      hmi_apis::messageType::request;
    (*navi_show_tbt)[strings::params][strings::protocol_version] =
      commands::CommandImpl::protocol_version_;
    (*navi_show_tbt)[strings::params][strings::protocol_type] =
      commands::CommandImpl::hmi_protocol_type_;
    (*navi_show_tbt)[strings::params][strings::correlation_id] =
      ApplicationManagerImpl::instance()->GetNextHMICorrelationID();
    (*navi_show_tbt)[strings::msg_params] = (*app->tbt_show_command());
    ApplicationManagerImpl::instance()->ManageHMICommand(navi_show_tbt);
  }
}

void MessageHelper::SendAddCommandRequestToHMI(const Application* app) {
  if (!app) {
    return;
  }

  const CommandsMap& commands = app->commands_map();
  CommandsMap::const_iterator i = commands.begin();
  for (; commands.end() != i; ++i) {
    // UI Interface
    if ((*i->second).keyExists(strings::menu_params)) {
      smart_objects::SmartObject* ui_command = new smart_objects::SmartObject(
        smart_objects::SmartType_Map);

      if (!ui_command) {
        return;
      }

      (*ui_command)[strings::params][strings::function_id] =
        hmi_apis::FunctionID::UI_AddCommand;
      (*ui_command)[strings::params][strings::message_type] =
        hmi_apis::messageType::request;
      (*ui_command)[strings::params][strings::protocol_version] =
        commands::CommandImpl::protocol_version_;
      (*ui_command)[strings::params][strings::protocol_type] =
        commands::CommandImpl::hmi_protocol_type_;
      (*ui_command)[strings::params][strings::correlation_id] =
        ApplicationManagerImpl::instance()->GetNextHMICorrelationID();

      smart_objects::SmartObject msg_params = smart_objects::SmartObject(
          smart_objects::SmartType_Map);
      msg_params[strings::cmd_id] = i->first;
      msg_params[strings::menu_params] = (*i->second)[strings::menu_params];
      msg_params[strings::app_id] = app->app_id();

      if (((*i->second)[strings::cmd_icon].keyExists(strings::value))
          && (0 < (*i->second)[strings::cmd_icon][strings::value].length())) {
        msg_params[strings::cmd_icon] = (*i->second)[strings::cmd_icon];
        msg_params[strings::cmd_icon][strings::value] =
            (*i->second)[strings::cmd_icon][strings::value].asString();
      }
      (*ui_command)[strings::msg_params] = msg_params;

      ApplicationManagerImpl::instance()->ManageHMICommand(ui_command);
    }

    // VR Interface
    if ((*i->second).keyExists(strings::vr_commands)) {
      SendAddVRCommandToHMI(i->first, (*i->second)[strings::vr_commands],
                            app->app_id());
    }
  }
}

smart_objects::SmartObject* MessageHelper::CreateChangeRegistration(
  int function_id, int language, unsigned int app_id) {
  smart_objects::SmartObject* command = new smart_objects::SmartObject(
    smart_objects::SmartType_Map);
  if (!command) {
    return NULL;
  }
  smart_objects::SmartObject& params = *command;

  params[strings::params][strings::message_type] =
    hmi_apis::messageType::request;
  params[strings::params][strings::protocol_version] =
    commands::CommandImpl::protocol_version_;
  params[strings::params][strings::protocol_type] =
    commands::CommandImpl::hmi_protocol_type_;

  params[strings::params][strings::function_id] = function_id;

  params[strings::params][strings::correlation_id] =
    ApplicationManagerImpl::instance()->GetNextHMICorrelationID();

  smart_objects::SmartObject msg_params = smart_objects::SmartObject(
      smart_objects::SmartType_Map);
  msg_params[strings::language] = language;
  msg_params[strings::app_id] = app_id;

  params[strings::msg_params] = msg_params;
  return command;
}

void MessageHelper::SendChangeRegistrationRequestToHMI(const Application* app) {
  if (mobile_apis::Language::INVALID_ENUM != app->language()
      && ApplicationManagerImpl::instance()->active_vr_language()
      != app->language()) {
    smart_objects::SmartObject* vr_command = CreateChangeRegistration(
          hmi_apis::FunctionID::VR_ChangeRegistration, app->language(),
          app->app_id());

    if (!vr_command) {
      return;
    }
    ApplicationManagerImpl::instance()->ManageHMICommand(vr_command);
  }

  if (mobile_apis::Language::INVALID_ENUM != app->language()
      && ApplicationManagerImpl::instance()->active_tts_language()
      != app->language()) {
    smart_objects::SmartObject* tts_command = CreateChangeRegistration(
          hmi_apis::FunctionID::TTS_ChangeRegistration, app->language(),
          app->app_id());

    if (!tts_command) {
      return;
    }
    ApplicationManagerImpl::instance()->ManageHMICommand(tts_command);
  }

  if (mobile_apis::Language::INVALID_ENUM != app->language()
      && ApplicationManagerImpl::instance()->active_ui_language()
      != app->ui_language()) {
    smart_objects::SmartObject* ui_command = CreateChangeRegistration(
          hmi_apis::FunctionID::UI_ChangeRegistration, app->ui_language(),
          app->app_id());

    if (!ui_command) {
      return;
    }
    ApplicationManagerImpl::instance()->ManageHMICommand(ui_command);
  }
}

void MessageHelper::SendAddVRCommandToHMI(
  unsigned int cmd_id, const smart_objects::SmartObject& vr_commands,
  unsigned int app_id) {
  smart_objects::SmartObject* vr_command = new smart_objects::SmartObject(
    smart_objects::SmartType_Map);

  if (!vr_command) {
    return;
  }

  (*vr_command)[strings::params][strings::function_id] =
    hmi_apis::FunctionID::VR_AddCommand;
  (*vr_command)[strings::params][strings::message_type] =
    hmi_apis::messageType::request;
  (*vr_command)[strings::params][strings::protocol_version] =
    commands::CommandImpl::protocol_version_;
  (*vr_command)[strings::params][strings::protocol_type] =
    commands::CommandImpl::hmi_protocol_type_;
  (*vr_command)[strings::params][strings::correlation_id] =
    ApplicationManagerImpl::instance()->GetNextHMICorrelationID();

  smart_objects::SmartObject msg_params = smart_objects::SmartObject(
      smart_objects::SmartType_Map);
  if (0 != cmd_id) {
    msg_params[strings::cmd_id] = cmd_id;
  }
  msg_params[strings::vr_commands] = vr_commands;
  msg_params[strings::app_id] = app_id;
  (*vr_command)[strings::msg_params] = msg_params;

  ApplicationManagerImpl::instance()->ManageHMICommand(vr_command);
}

void MessageHelper::SendAddSubMenuRequestToHMI(const Application* app) {
  if (!app) {
    return;
  }

  const SubMenuMap& sub_menu = app->sub_menu_map();
  SubMenuMap::const_iterator i = sub_menu.begin();
  for (; sub_menu.end() != i; ++i) {
    smart_objects::SmartObject* ui_sub_menu = new smart_objects::SmartObject(
      smart_objects::SmartType_Map);

    if (!ui_sub_menu) {
      return;
    }

    (*ui_sub_menu)[strings::params][strings::function_id] =
      hmi_apis::FunctionID::UI_AddSubMenu;
    (*ui_sub_menu)[strings::params][strings::message_type] =
      hmi_apis::messageType::request;
    (*ui_sub_menu)[strings::params][strings::protocol_version] =
      commands::CommandImpl::protocol_version_;
    (*ui_sub_menu)[strings::params][strings::protocol_type] =
      commands::CommandImpl::hmi_protocol_type_;
    (*ui_sub_menu)[strings::params][strings::correlation_id] =
      ApplicationManagerImpl::instance()->GetNextHMICorrelationID();

    smart_objects::SmartObject msg_params = smart_objects::SmartObject(
        smart_objects::SmartType_Map);

    msg_params[strings::menu_id] = i->first;
    msg_params[strings::menu_params][strings::position] =
      (*i->second)[strings::position];
    msg_params[strings::menu_params][strings::menu_name] =
      (*i->second)[strings::menu_name];
    msg_params[strings::app_id] = app->app_id();
    (*ui_sub_menu)[strings::msg_params] = msg_params;

    ApplicationManagerImpl::instance()->ManageHMICommand(ui_sub_menu);
  }
}

void MessageHelper::RemoveAppDataFromHMI(Application* const app) {
  SendDeleteCommandRequestToHMI(app);
  SendDeleteSubMenuRequestToHMI(app);
  ResetGlobalproperties(app);
}

void MessageHelper::SendOnAppUnregNotificationToHMI(Application* const app) {
  smart_objects::SmartObject* notification = new smart_objects::SmartObject(
    smart_objects::SmartType_Map);
  if (!notification) {
    return;
  }

  smart_objects::SmartObject& message = *notification;

  message[strings::params][strings::function_id] =
    hmi_apis::FunctionID::BasicCommunication_OnAppUnregistered;

  message[strings::params][strings::message_type] = MessageType::kNotification;

  message[strings::msg_params][strings::app_id] = app->app_id();

  ApplicationManagerImpl::instance()->ManageHMICommand(&message);
}

void MessageHelper::SendDeleteCommandRequestToHMI(Application* const app) {
  if (!app) {
    return;
  }

  const CommandsMap& commands = app->commands_map();
  CommandsMap::const_iterator i = commands.begin();
  for (; commands.end() != i; ++i) {
    if ((*i->second).keyExists(strings::menu_params)) {
      SendRemoveCommandToHMI(hmi_apis::FunctionID::UI_DeleteCommand, i->first,
                             app->app_id());
    }

    if ((*i->second).keyExists(strings::vr_commands)) {
      SendRemoveCommandToHMI(hmi_apis::FunctionID::VR_DeleteCommand, i->first,
                             app->app_id());
    }
  }
}

void MessageHelper::SendRemoveCommandToHMI(int function_id, int command_id,
    unsigned int app_id) {
  smart_objects::SmartObject* delete_cmd = new smart_objects::SmartObject(
    smart_objects::SmartType_Map);

  if (!delete_cmd) {
    return;
  }

  (*delete_cmd)[strings::params][strings::function_id] = function_id;
  (*delete_cmd)[strings::params][strings::message_type] =
    hmi_apis::messageType::request;
  (*delete_cmd)[strings::params][strings::protocol_version] =
    commands::CommandImpl::protocol_version_;
  (*delete_cmd)[strings::params][strings::protocol_type] =
    commands::CommandImpl::hmi_protocol_type_;
  (*delete_cmd)[strings::params][strings::correlation_id] =
    ApplicationManagerImpl::instance()->GetNextHMICorrelationID();

  smart_objects::SmartObject msg_params = smart_objects::SmartObject(
      smart_objects::SmartType_Map);
  msg_params[strings::cmd_id] = command_id;
  msg_params[strings::app_id] = app_id;
  (*delete_cmd)[strings::msg_params] = msg_params;
  ApplicationManagerImpl::instance()->ManageHMICommand(delete_cmd);
}

void MessageHelper::SendDeleteSubMenuRequestToHMI(Application* const app) {
  if (!app) {
    return;
  }

  const SubMenuMap& sub_menu = app->sub_menu_map();
  SubMenuMap::const_iterator i = sub_menu.begin();
  for (; sub_menu.end() != i; ++i) {
    smart_objects::SmartObject* delete_sub_menu =
      new smart_objects::SmartObject(smart_objects::SmartType_Map);

    if (!delete_sub_menu) {
      return;
    }

    (*delete_sub_menu)[strings::params][strings::function_id] =
      hmi_apis::FunctionID::UI_DeleteSubMenu;
    (*delete_sub_menu)[strings::params][strings::message_type] =
      hmi_apis::messageType::request;
    (*delete_sub_menu)[strings::params][strings::protocol_version] =
      commands::CommandImpl::protocol_version_;
    (*delete_sub_menu)[strings::params][strings::protocol_type] =
      commands::CommandImpl::hmi_protocol_type_;
    (*delete_sub_menu)[strings::params][strings::correlation_id] =
      ApplicationManagerImpl::instance()->GetNextHMICorrelationID();

    smart_objects::SmartObject msg_params = smart_objects::SmartObject(
        smart_objects::SmartType_Map);

    msg_params[strings::menu_id] = i->first;
    msg_params[strings::menu_params][strings::position] =
      (*i->second)[strings::position];
    msg_params[strings::menu_params][strings::menu_name] =
      (*i->second)[strings::menu_name];
    msg_params[strings::app_id] = app->app_id();
    (*delete_sub_menu)[strings::msg_params] = msg_params;

    ApplicationManagerImpl::instance()->ManageHMICommand(delete_sub_menu);
  }
}

void MessageHelper::SendActivateAppToHMI(Application* const app) {
  smart_objects::SmartObject* message = new smart_objects::SmartObject(
    smart_objects::SmartType_Map);
  if (!message) {
    return;
  }

  (*message)[strings::params][strings::function_id] =
    hmi_apis::FunctionID::BasicCommunication_ActivateApp;
  (*message)[strings::params][strings::message_type] = MessageType::kRequest;
  (*message)[strings::params][strings::correlation_id] =
    ApplicationManagerImpl::instance()->GetNextHMICorrelationID();
  (*message)[strings::msg_params][strings::app_id] = app->app_id();

  ApplicationManagerImpl::instance()->ManageHMICommand(message);
}

smart_objects::SmartObject* MessageHelper::CreateNegativeResponse(
  unsigned int connection_key, int function_id, unsigned int correlation_id,
  int result_code) {
  smart_objects::SmartObject* response = new smart_objects::SmartObject(
    smart_objects::SmartType_Map);
  smart_objects::SmartObject& response_data = *response;
  response_data[strings::params][strings::function_id] = function_id;
  response_data[strings::params][strings::message_type] =
    mobile_apis::messageType::response;
  response_data[strings::params][strings::correlation_id] = correlation_id;
  response_data[strings::params][strings::protocol_type] =
    commands::CommandImpl::mobile_protocol_type_;
  response_data[strings::params][strings::protocol_version] =
    commands::CommandImpl::protocol_version_;
  response_data[strings::msg_params][strings::result_code] = result_code;
  response_data[strings::msg_params][strings::success] = false;
  response_data[strings::params][strings::connection_key] = connection_key;

  return response;
}

void MessageHelper::ResetGlobalproperties(Application* const app) {
  // reset help_promt
  const std::vector<std::string>& help_promt = profile::Profile::instance()
      ->help_promt();

  smart_objects::SmartObject so_help_promt = smart_objects::SmartObject(
        smart_objects::SmartType_Array);

  for (unsigned int i = 0; i < help_promt.size(); ++i) {
    smart_objects::SmartObject helpPrompt = smart_objects::SmartObject(
        smart_objects::SmartType_Map);
    helpPrompt[strings::text] = help_promt[i];
    so_help_promt[i] = helpPrompt;
  }

  app->set_help_prompt(so_help_promt);

  // reset timeout prompt
  const std::vector<std::string>& time_out_promt = profile::Profile::instance()
      ->time_out_promt();

  smart_objects::SmartObject so_time_out_promt = smart_objects::SmartObject(
        smart_objects::SmartType_Array);

  for (unsigned int i = 0; i < time_out_promt.size(); ++i) {
    smart_objects::SmartObject timeoutPrompt = smart_objects::SmartObject(
          smart_objects::SmartType_Map);
    timeoutPrompt[strings::text] = time_out_promt[i];
    so_time_out_promt[i] = timeoutPrompt;
  }

  app->set_timeout_prompt(so_time_out_promt);

  // reset VR help title
  smart_objects::SmartObject help_title(app->name());
  app->set_vr_help_title(help_title);

  // reset VR help items
  const CommandsMap& cmdMap = app->commands_map();
  smart_objects::SmartObject vr_help_items;

  int index = 0;
  CommandsMap::const_iterator command_it = cmdMap.begin();

  for (; cmdMap.end() != command_it; ++command_it) {
    if (true == (*command_it->second).keyExists(strings::vr_commands)) {
      // use only first
      vr_help_items[index++] = (*command_it->second)[strings::vr_commands][0];
    }
  }

  app->set_vr_help(vr_help_items);

  // send global properties
  SendGlobalPropertiesToHMI(app);
}

mobile_apis::Result::eType MessageHelper::VerifyImageFiles(
  smart_objects::SmartObject& message, const Application* app) {
  if (NsSmartDeviceLink::NsSmartObjects::SmartType_Array == message.getType()) {
    for (int i = 0; i < message.length(); ++i) {
      mobile_apis::Result::eType res = VerifyImageFiles(message[i], app);
      if (mobile_apis::Result::SUCCESS != res) {
        return res;
      }
    }
  } else if (NsSmartDeviceLink::NsSmartObjects::SmartType_Map
             == message.getType()) {
    if (message.keyExists(strings::image_type)) {
      mobile_apis::Result::eType verification_result =
                                                  VerifyImage(message, app);

      if (mobile_apis::Result::SUCCESS != verification_result) {
        return verification_result; // exit point
      }
    } else {
      std::set < std::string > keys = message.enumerate();

      for (std::set<std::string>::const_iterator key = keys.begin();
           key != keys.end(); ++key) {
        if (strings::soft_buttons != (*key)) {
          mobile_apis::Result::eType res = VerifyImageFiles(message[*key], app);
          if (mobile_apis::Result::SUCCESS != res) {
            return res;
          }
        }
      }
    }
  }  // all other types shoudn't be processed

  return mobile_apis::Result::SUCCESS;
}

mobile_apis::Result::eType MessageHelper::VerifyImage(
    smart_objects::SmartObject& image, const Application* app) {
  const std::string& file_name = image[strings::value];

  std::string relative_file_path = app->name();
  relative_file_path += "/";
  relative_file_path += file_name;

  std::string full_file_path = file_system::FullPath(relative_file_path);

  if (!file_system::FileExists(full_file_path)) {
    return mobile_apis::Result::INVALID_DATA;
  }

  if (!ApplicationManagerImpl::instance()->VerifyImageType(
      static_cast<mobile_apis::ImageType::eType>(image[strings::image_type]
          .asInt()))) {
    return mobile_apis::Result::UNSUPPORTED_RESOURCE;
  }

  image[strings::value] = full_file_path;

  return mobile_apis::Result::SUCCESS;
}

mobile_apis::Result::eType MessageHelper::ProcessSoftButtons(
    smart_objects::SmartObject& message_params, const Application* app) {
  if (!message_params.keyExists(strings::soft_buttons)) {
    return mobile_apis::Result::SUCCESS;
  }

  const smart_objects::SmartObject* soft_button_capabilities =
     ApplicationManagerImpl::instance()->soft_button_capabilities();
  bool image_supported = false;
  if (soft_button_capabilities) {
    image_supported =
         (*soft_button_capabilities)[hmi_response::image_supported].asBool();
  }

  smart_objects::SmartObject& request_soft_buttons =
      message_params[strings::soft_buttons];

  smart_objects::SmartObject soft_buttons = smart_objects::SmartObject(
      smart_objects::SmartType_Array);

  int j = 0;
  for (int i = 0; i < request_soft_buttons.length(); ++i) {
    switch (request_soft_buttons[i][strings::type].asInt()) {
      case mobile_apis::SoftButtonType::SBT_IMAGE: {
        if (!image_supported) {
          continue;
        }

        if (request_soft_buttons[i].keyExists(strings::image)) {
          mobile_apis::Result::eType verification_result = VerifyImage(
              request_soft_buttons[i][strings::image], app);

          if (mobile_apis::Result::SUCCESS != verification_result) {
            if (mobile_apis::Result::UNSUPPORTED_RESOURCE ==
                verification_result) {
              request_soft_buttons[i].erase(strings::image);
            }
            return verification_result;
          }
        } else {
          return mobile_apis::Result::INVALID_DATA;
        }
        break;
      }
      case mobile_apis::SoftButtonType::SBT_TEXT: {
        if (!request_soft_buttons[i].keyExists(strings::text)) {
          continue;
        }
        break;
      }
      case mobile_apis::SoftButtonType::SBT_BOTH: {
        bool text_exist = request_soft_buttons[i].keyExists(strings::text);

        bool image_exist = false;
        if (image_supported) {
          image_exist = request_soft_buttons[i].keyExists(strings::image);
        }

        if ((!image_exist) && (!text_exist)) {
          return mobile_apis::Result::INVALID_DATA;
        }

        if (image_exist) {
          mobile_apis::Result::eType verification_result = VerifyImage(
              request_soft_buttons[i][strings::image], app);

          if (mobile_apis::Result::SUCCESS != verification_result) {
            if (!text_exist) {
              return mobile_apis::Result::INVALID_DATA;
            }
            if (mobile_apis::Result::UNSUPPORTED_RESOURCE ==
                verification_result) {
              request_soft_buttons[i].erase(strings::image);
              return verification_result;
            }
          }
        }
        break;
      }
      default: {
        continue;
        break;
      }
    }

    soft_buttons[j] = request_soft_buttons[i];

    if (!soft_buttons[j].keyExists(strings::system_action)) {
      soft_buttons[j][strings::system_action] =
          mobile_apis::SystemAction::DEFAULT_ACTION;
    }

    ++j;
  }

  request_soft_buttons = soft_buttons;

  if (0 == request_soft_buttons.length()) {
    message_params.erase(strings::soft_buttons);
  }

  return mobile_apis::Result::SUCCESS;
}

// TODO(VS): change printf to logger
bool MessageHelper::VerifyApplicationName(
  smart_objects::SmartObject& msg_params) {
  for (int i = 0; i < msg_params[strings::tts_name].length(); ++i) {
    const std::string& tts_name = msg_params[strings::tts_name][i].asString();
    if ((tts_name[0] == '\n') || (tts_name[0] == ' ') ||
        ((tts_name[0] == '\\') && (tts_name[1] == 'n'))) {
      printf("Invalid characters in tts name.\n");
      return false;
    }
  }

  const std::string& name = msg_params[strings::app_name].asString();

  if ((name[0] == '\n') || (name[0] == ' ') ||
      ((name[0] == '\\') && (name[1] == 'n'))) {
    printf("Invalid characters in application name.\n");
    return false;
  }

  return true;
}

// TODO(AK): change printf to logger
bool MessageHelper::PrintSmartObject(smart_objects::SmartObject& object) {
  static unsigned int tab = 0;
  std::string tab_buffer;

  if (tab == 0) {
    printf("\n-------------------------------------------------------------");
  }

  for (unsigned int i = 0; i < tab; ++i) {
    tab_buffer += "\t";
  }

  switch (object.getType()) {
    case NsSmartDeviceLink::NsSmartObjects::SmartType_Array: {
      for (int i = 0; i < object.length(); i++) {
        ++tab;

        printf("\n%s%d: ", tab_buffer.c_str(), i);
        if (!PrintSmartObject(object[i])) {
          printf("\n");
          return false;
        }
      }
      break;
    }
    case NsSmartDeviceLink::NsSmartObjects::SmartType_Map: {
      std::set < std::string > keys = object.enumerate();

      for (std::set<std::string>::const_iterator key = keys.begin();
           key != keys.end(); key++) {
        ++tab;

        printf("\n%s%s: ", tab_buffer.c_str(), (*key).c_str());
        if (!PrintSmartObject(object[*key])) {
          printf("\n");
          return false;
        }
      }
      break;
    }
    case NsSmartDeviceLink::NsSmartObjects::SmartType_Boolean:
      object.asBool() ? printf("true\n") : printf("false\n");
      break;
    case NsSmartDeviceLink::NsSmartObjects::SmartType_Double: {
      printf("%f", object.asDouble());
      break;
    }
    case NsSmartDeviceLink::NsSmartObjects::SmartType_Integer:
      printf("%d", object.asInt());
      break;
    case NsSmartDeviceLink::NsSmartObjects::SmartType_String:
      printf("%s", object.asString().c_str());
      break;
    case NsSmartDeviceLink::NsSmartObjects::SmartType_Character:
      printf("%c", object.asChar());
      break;
    default:
      printf("PrintSmartObject - default case\n");
      break;
  }

  if (0 != tab) {
    --tab;
  } else {
    printf("\n-------------------------------------------------------------\n");
  }

  return true;
}

}  //  namespace application_manager
