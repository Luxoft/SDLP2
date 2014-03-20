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

#include "application_manager/mobile_command_factory.h"
#include "application_manager/commands/mobile/add_command_request.h"
#include "application_manager/commands/mobile/add_command_response.h"
#include "application_manager/commands/mobile/delete_command_request.h"
#include "application_manager/commands/mobile/delete_command_response.h"
#include "application_manager/commands/mobile/add_sub_menu_request.h"
#include "application_manager/commands/mobile/add_sub_menu_response.h"
#include "application_manager/commands/mobile/alert_request.h"
#include "application_manager/commands/mobile/alert_response.h"
#include "application_manager/commands/mobile/alert_maneuver_request.h"
#include "application_manager/commands/mobile/alert_maneuver_response.h"
#include "application_manager/commands/mobile/change_registration_request.h"
#include "application_manager/commands/mobile/change_registration_response.h"
#include "application_manager/commands/mobile/create_interaction_choice_set_request.h"
#include "application_manager/commands/mobile/create_interaction_choice_set_response.h"
#include "application_manager/commands/mobile/delete_file_request.h"
#include "application_manager/commands/mobile/delete_file_response.h"
#include "application_manager/commands/mobile/delete_interaction_choice_set_request.h"
#include "application_manager/commands/mobile/delete_interaction_choice_set_response.h"
#include "application_manager/commands/mobile/delete_sub_menu_request.h"
#include "application_manager/commands/mobile/delete_sub_menu_response.h"
#include "application_manager/commands/mobile/dial_number_request.h"
#include "application_manager/commands/mobile/encoded_sync_pdata_request.h"
#include "application_manager/commands/mobile/encoded_sync_pdata_response.h"
#include "application_manager/commands/mobile/end_audio_pass_thru_request.h"
#include "application_manager/commands/mobile/end_audio_pass_thru_response.h"
#include "application_manager/commands/mobile/generic_response.h"
#include "application_manager/commands/mobile/get_dtcs_request.h"
#include "application_manager/commands/mobile/get_dtcs_response.h"
#include "application_manager/commands/mobile/get_vehicle_data_request.h"
#include "application_manager/commands/mobile/get_vehicle_data_response.h"
#include "application_manager/commands/mobile/list_files_request.h"
#include "application_manager/commands/mobile/list_files_response.h"
#include "application_manager/commands/mobile/on_app_interface_unregistered_notification.h"
#include "application_manager/commands/mobile/on_audio_pass_thru_notification.h"
#include "application_manager/commands/mobile/on_button_event_notification.h"
#include "application_manager/commands/mobile/on_button_press_notification.h"
#include "application_manager/commands/mobile/on_driver_distraction_notification.h"
#include "application_manager/commands/mobile/on_encoded_sync_pdata_notification.h"
#include "application_manager/commands/mobile/on_hmi_status_notification.h"
#include "application_manager/commands/mobile/on_language_change_notification.h"
#include "application_manager/commands/mobile/on_command_notification.h"
#include "application_manager/commands/mobile/on_permissions_change_notification.h"
#include "application_manager/commands/mobile/on_tbt_client_state_notification.h"
#include "application_manager/commands/mobile/on_vehicle_data_notification.h"
#include "application_manager/commands/mobile/perform_audio_pass_thru_request.h"
#include "application_manager/commands/mobile/perform_audio_pass_thru_response.h"
#include "application_manager/commands/mobile/perform_interaction_request.h"
#include "application_manager/commands/mobile/perform_interaction_response.h"
#include "application_manager/commands/mobile/put_file_request.h"
#include "application_manager/commands/mobile/put_file_response.h"
#include "application_manager/commands/mobile/read_did_request.h"
#include "application_manager/commands/mobile/read_did_response.h"
#include "application_manager/commands/mobile/register_app_interface_request.h"
#include "application_manager/commands/mobile/register_app_interface_response.h"
#include "application_manager/commands/mobile/reset_global_properties_request.h"
#include "application_manager/commands/mobile/reset_global_properties_response.h"
#include "application_manager/commands/mobile/scrollable_message_request.h"
#include "application_manager/commands/mobile/scrollable_message_response.h"
#include "application_manager/commands/mobile/set_display_layout_request.h"
#include "application_manager/commands/mobile/set_display_layout_response.h"
#include "application_manager/commands/mobile/set_global_properties_request.h"
#include "application_manager/commands/mobile/set_global_properties_response.h"
#include "application_manager/commands/mobile/set_icon_request.h"
#include "application_manager/commands/mobile/set_icon_response.h"
#include "application_manager/commands/mobile/set_media_clock_timer_request.h"
#include "application_manager/commands/mobile/set_media_clock_timer_response.h"
#include "application_manager/commands/mobile/show_constant_tbt_request.h"
#include "application_manager/commands/mobile/show_constant_tbt_response.h"
#include "application_manager/commands/mobile/show_request.h"
#include "application_manager/commands/mobile/show_response.h"
#include "application_manager/commands/mobile/slider_request.h"
#include "application_manager/commands/mobile/slider_response.h"
#include "application_manager/commands/mobile/speak_request.h"
#include "application_manager/commands/mobile/speak_response.h"
#include "application_manager/commands/mobile/subscribe_button_request.h"
#include "application_manager/commands/mobile/subscribe_button_response.h"
#include "application_manager/commands/mobile/subscribe_vehicle_data_request.h"
#include "application_manager/commands/mobile/subscribe_vehicle_data_response.h"
#include "application_manager/commands/mobile/unregister_app_interface_request.h"
#include "application_manager/commands/mobile/unregister_app_interface_response.h"
#include "application_manager/commands/mobile/unsubscribe_button_request.h"
#include "application_manager/commands/mobile/unsubscribe_button_response.h"
#include "application_manager/commands/mobile/unsubscribe_vehicle_data_request.h"
#include "application_manager/commands/mobile/unsubscribe_vehicle_data_response.h"
#include "application_manager/commands/mobile/update_turn_list_request.h"
#include "application_manager/commands/mobile/update_turn_list_response.h"
#include "application_manager/commands/mobile/sync_pdata_request.h"
#include "application_manager/commands/mobile/sync_pdata_response.h"
#include "application_manager/commands/mobile/on_sync_pdata_notification.h"

#include "application_manager/commands/mobile/sdlp_add_profile_request.h"
#include "application_manager/commands/mobile/sdlp_remove_profile_request.h"
#include "application_manager/commands/mobile/sdlp_load_profile_request.h"
#include "application_manager/commands/mobile/sdlp_unload_profile_request.h"
#include "application_manager/commands/mobile/sdlp_app_to_profile_request.h"
#include "application_manager/commands/mobile/sdlp_app_state_changed_request.h"
#include "application_manager/commands/mobile/sdlp_response.h"
#include "application_manager/commands/mobile/sdlp_notification.h"

#include "interfaces/MOBILE_API.h"

namespace application_manager {

CommandSharedPtr MobileCommandFactory::CreateCommand(
    const MessageSharedPtr& message) {
  CommandSharedPtr command;

  switch ((*message)[strings::params][strings::function_id].asInt()) {
    case mobile_apis::FunctionID::RegisterAppInterfaceID: {
      if ((*message)[strings::params][strings::message_type]
          == MessageType::kRequest) {
        command.reset(new commands::RegisterAppInterfaceRequest(message));
      } else {
        command.reset(new commands::RegisterAppInterfaceResponse(message));
      }
      break;
    }
    case mobile_apis::FunctionID::UnregisterAppInterfaceID: {
      if ((*message)[strings::params][strings::message_type]
          == MessageType::kRequest) {
        command.reset(new commands::UnregisterAppInterfaceRequest(message));
      } else {
        command.reset(new commands::UnregisterAppInterfaceResponse(message));
      }
      break;
    }
    case mobile_apis::FunctionID::SetGlobalPropertiesID: {
      if ((*message)[strings::params][strings::message_type]
          == MessageType::kResponse) {
        command.reset(new commands::SetGlobalPropertiesResponse(message));
      } else {
        command.reset(new commands::SetGlobalPropertiesRequest(message));
      }
      break;
    }
    case mobile_apis::FunctionID::ResetGlobalPropertiesID: {
      if ((*message)[strings::params][strings::message_type]
          == MessageType::kResponse) {
        command.reset(new commands::ResetGlobalPropertiesResponse(message));
      } else {
        command.reset(new commands::ResetGlobalPropertiesRequest(message));
      }
      break;
    }
    case mobile_apis::FunctionID::AddCommandID: {
      if ((*message)[strings::params][strings::message_type]
          == MessageType::kResponse) {
        command.reset(new commands::AddCommandResponse(message));
      } else {
        command.reset(new commands::AddCommandRequest(message));
      }
      break;
    }
    case mobile_apis::FunctionID::DeleteCommandID: {
      if ((*message)[strings::params][strings::message_type]
          == MessageType::kResponse) {
        command.reset(new commands::DeleteCommandResponse(message));
      } else {
        command.reset(new commands::DeleteCommandRequest(message));
      }
      break;
    }
    case mobile_apis::FunctionID::AddSubMenuID: {
      if ((*message)[strings::params][strings::message_type]
          == MessageType::kResponse) {
        command.reset(new commands::AddSubMenuResponse(message));
      } else {
        command.reset(new commands::AddSubMenuRequest(message));
      }
      break;
    }
    case mobile_apis::FunctionID::DeleteSubMenuID: {
      if ((*message)[strings::params][strings::message_type]
          == MessageType::kResponse) {
        command.reset(new commands::DeleteSubMenuResponse(message));
      } else {
        command.reset(new commands::DeleteSubMenuRequest(message));
      }
      break;
    }
    case mobile_apis::FunctionID::DeleteInteractionChoiceSetID: {
      if ((*message)[strings::params][strings::message_type]
          == MessageType::kResponse) {
        command.reset(
            new commands::DeleteInteractionChoiceSetResponse(message));
      } else {
        command.reset(new commands::DeleteInteractionChoiceSetRequest(message));
      }
      break;
    }
    case mobile_apis::FunctionID::AlertID: {
      if ((*message)[strings::params][strings::message_type]
          == MessageType::kResponse) {
        command.reset(new commands::AlertResponse(message));
      } else {
        command.reset(new commands::AlertRequest(message));
      }
      break;
    }
    case mobile_apis::FunctionID::SpeakID: {
      if ((*message)[strings::params][strings::message_type]
          == MessageType::kResponse) {
        command.reset(new commands::SpeakResponse(message));
      } else {
        command.reset(new commands::SpeakRequest(message));
      }
      break;
    }
    case mobile_apis::FunctionID::SliderID: {
      if ((*message)[strings::params][strings::message_type]
          == MessageType::kResponse) {
        command.reset(new commands::SliderResponse(message));
      } else {
        command.reset(new commands::SliderRequest(message));
      }
      break;
    }
    case mobile_apis::FunctionID::PerformAudioPassThruID: {
      if ((*message)[strings::params][strings::message_type]
          == MessageType::kResponse) {
        command.reset(new commands::PerformAudioPassThruResponse(message));
      } else {
        command.reset(new commands::PerformAudioPassThruRequest(message));
      }
      break;
    }
    case mobile_apis::FunctionID::CreateInteractionChoiceSetID: {
      if ((*message)[strings::params][strings::message_type]
          == MessageType::kResponse) {
        command.reset(
            new commands::CreateInteractionChoiceSetResponse(message));
      } else {
        command.reset(new commands::CreateInteractionChoiceSetRequest(message));
      }
      break;
    }
    case mobile_apis::FunctionID::PerformInteractionID: {
      if ((*message)[strings::params][strings::message_type]
          == MessageType::kResponse) {
        command.reset(new commands::PerformInteractionResponse(message));
      } else {
        command.reset(new commands::PerformInteractionRequest(message));
      }
      break;
    }
    case mobile_apis::FunctionID::EndAudioPassThruID: {
      if ((*message)[strings::params][strings::message_type]
          == MessageType::kResponse) {
        command.reset(new commands::EndAudioPassThruResponse(message));
      } else {
        command.reset(new commands::EndAudioPassThruRequest(message));
      }
      break;
    }
    case mobile_apis::FunctionID::PutFileID: {
      if ((*message)[strings::params][strings::message_type]
          == MessageType::kResponse) {
        command.reset(new commands::PutFileResponse(message));
      } else {
        command.reset(new commands::PutFileRequest(message));
      }
      break;
    }
    case mobile_apis::FunctionID::DeleteFileID: {
      if ((*message)[strings::params][strings::message_type]
          == MessageType::kResponse) {
        command.reset(new commands::DeleteFileResponse(message));
      } else {
        command.reset(new commands::DeleteFileRequest(message));
      }
      break;
    }
    case mobile_apis::FunctionID::ListFilesID: {
      if ((*message)[strings::params][strings::message_type]
          == MessageType::kResponse) {
        command.reset(new commands::ListFilesResponse(message));
      } else {
        command.reset(new commands::ListFilesRequest(message));
      }
      break;
    }
    case mobile_apis::FunctionID::SubscribeButtonID: {
      if ((*message)[strings::params][strings::message_type]
          == MessageType::kResponse) {
        command.reset(new commands::SubscribeButtonResponse(message));
      } else {
        command.reset(new commands::SubscribeButtonRequest(message));
      }
      break;
    }
    case mobile_apis::FunctionID::UnsubscribeButtonID: {
      if ((*message)[strings::params][strings::message_type]
          == MessageType::kResponse) {
        command.reset(new commands::UnsubscribeButtonResponse(message));
      } else {
        command.reset(new commands::UnsubscribeButtonRequest(message));
      }
      break;
    }
    case mobile_apis::FunctionID::ShowConstantTBTID: {
      if ((*message)[strings::params][strings::message_type]
          == MessageType::kResponse) {
        command.reset(new commands::ShowConstantTBTResponse(message));
      } else {
        command.reset(new commands::ShowConstantTBTRequest(message));
      }
      break;
    }
    case mobile_apis::FunctionID::ShowID: {
      if ((*message)[strings::params][strings::message_type]
          == MessageType::kResponse) {
        command.reset(new commands::ShowResponse(message));
      } else {
        command.reset(new commands::ShowRequest(message));
      }
      break;
    }
    case mobile_apis::FunctionID::SubscribeVehicleDataID: {
      if ((*message)[strings::params][strings::message_type]
          == MessageType::kResponse) {
        command.reset(new commands::SubscribeVehicleDataResponse(message));
      } else {
        command.reset(new commands::SubscribeVehicleDataRequest(message));
      }
      break;
    }
    case mobile_apis::FunctionID::SyncPDataID: {
      if ((*message)[strings::params][strings::message_type]
          == MessageType::kResponse) {
        command.reset(new commands::SyncPDataResponse(message));
      } else {
        command.reset(new commands::SyncPDataRequest(message));
      }
      break;
    }
    case mobile_apis::FunctionID::EncodedSyncPDataID: {
      if ((*message)[strings::params][strings::message_type]
          == MessageType::kResponse) {
        command.reset(new commands::EncodedSyncPDataResponse(message));
      } else {
        command.reset(new commands::EncodedSyncPDataRequest(message));
      }
      break;
    }
    case mobile_apis::FunctionID::UnsubscribeVehicleDataID: {
      if ((*message)[strings::params][strings::message_type]
          == MessageType::kResponse) {
        command.reset(new commands::UnsubscribeVehicleDataResponse(message));
      } else {
        command.reset(new commands::UnsubscribeVehicleDataRequest(message));
      }
      break;
    }
    case mobile_apis::FunctionID::ReadDIDID: {
      if ((*message)[strings::params][strings::message_type]
          == MessageType::kResponse) {
        command.reset(new commands::ReadDIDResponse(message));
      } else {
        command.reset(new commands::ReadDIDRequest(message));
      }
      break;
    }
    case mobile_apis::FunctionID::GetVehicleDataID: {
      if ((*message)[strings::params][strings::message_type]
          == MessageType::kResponse) {
        command.reset(new commands::GetVehicleDataResponse(message));
      } else {
        command.reset(new commands::GetVehicleDataRequest(message));
      }
      break;
    }
    case mobile_apis::FunctionID::ScrollableMessageID: {
      if ((*message)[strings::params][strings::message_type]
          == MessageType::kResponse) {
        command.reset(new commands::ScrollableMessageResponse(message));
      } else {
        command.reset(new commands::ScrollabelMessageRequest(message));
      }
      break;
    }
    case mobile_apis::FunctionID::AlertManeuverID: {
      if ((*message)[strings::params][strings::message_type]
          == MessageType::kResponse) {
        command.reset(new commands::AlertManeuverResponse(message));
      } else {
        command.reset(new commands::AlertManeuverRequest(message));
      }
      break;
    }
    case mobile_apis::FunctionID::SetAppIconID: {
      if ((*message)[strings::params][strings::message_type]
          == MessageType::kResponse) {
        command.reset(new commands::SetIconResponse(message));
      } else {
        command.reset(new commands::SetIconRequest(message));
      }
      break;
    }
    case mobile_apis::FunctionID::SetDisplayLayoutID: {
      if ((*message)[strings::params][strings::message_type]
          == MessageType::kResponse) {
        command.reset(new commands::SetDisplayLayoutResponse(message));
      } else {
        command.reset(new commands::SetDisplayLayoutRequest(message));
      }
      break;
    }
    case mobile_apis::FunctionID::UpdateTurnListID: {
      if ((*message)[strings::params][strings::message_type]
          == MessageType::kResponse) {
        command.reset(new commands::UpdateTurnListResponse(message));
      } else {
        command.reset(new commands::UpdateTurnListRequest(message));
      }
      break;
    }
    case mobile_apis::FunctionID::ChangeRegistrationID: {
      if ((*message)[strings::params][strings::message_type]
          == MessageType::kResponse) {
        command.reset(new commands::ChangeRegistrationResponse(message));
      } else {
        command.reset(new commands::ChangeRegistrationRequest(message));
      }
      break;
    }
    case mobile_apis::FunctionID::GetDTCsID: {
      if ((*message)[strings::params][strings::message_type]
          == MessageType::kResponse) {
        command.reset(new commands::GetDTCsResponse(message));
      } else {
        command.reset(new commands::GetDTCsRequest(message));
      }
      break;
    }
    case mobile_apis::FunctionID::SetMediaClockTimerID: {
      if ((*message)[strings::params][strings::message_type]
          == MessageType::kResponse) {
        command.reset(new commands::SetMediaClockTimerResponse(message));
      } else {
        command.reset(new commands::SetMediaClockRequest(message));
      }
      break;
    }
    case mobile_apis::FunctionID::OnButtonEventID: {
      command.reset(new commands::mobile::OnButtonEventNotification(message));
      break;
    }
    case mobile_apis::FunctionID::OnButtonPressID: {
      command.reset(new commands::mobile::OnButtonPressNotification(message));
      break;
    }
    case mobile_apis::FunctionID::OnAudioPassThruID: {
      command.reset(new commands::OnAudioPassThruNotification(message));
      break;
    }
    case mobile_apis::FunctionID::OnVehicleDataID: {
      command.reset(new commands::OnVehicleDataNotification(message));
      break;
    }
    case mobile_apis::FunctionID::OnAppInterfaceUnregisteredID: {
      command.reset(
          new commands::OnAppInterfaceUnregisteredNotification(message));
      break;
    }
    case mobile_apis::FunctionID::OnCommandID: {
      command.reset(new commands::OnCommandNotification(message));
      break;
    }
    case mobile_apis::FunctionID::OnTBTClientStateID: {
      command.reset(new commands::OnTBTClientStateNotification(message));
      break;
    }
    case mobile_apis::FunctionID::OnDriverDistractionID: {
      command.reset(
          new commands::mobile::OnDriverDistractionNotification(message));
      break;
    }
    case mobile_apis::FunctionID::OnLanguageChangeID: {
      command.reset(new commands::OnLanguageChangeNotification(message));
      break;
    }
    case mobile_apis::FunctionID::OnPermissionsChangeID: {
      command.reset(new commands::OnPermissionsChangeNotification(message));
      break;
    }
    case mobile_apis::FunctionID::OnHMIStatusID: {
      command.reset(new commands::OnHMIStatusNotification(message));
      break;
    }
    case mobile_apis::FunctionID::OnSyncPDataID: {
      command.reset(new commands::OnSyncPDataNotification(message));
      break;
    }
    // PROFILE MANAGER GOODNESS
    case mobile_apis::FunctionID::AddProfileID: {
      if ((*message)[strings::params][strings::message_type]
          == MessageType::kResponse) {
        command.reset(new commands::SDLPResponse(message));
      } else {
        command.reset(new commands::AddProfileRequest(message));
      }
      break;
    }
    case mobile_apis::FunctionID::RemoveProfileID: {
      if ((*message)[strings::params][strings::message_type]
          == MessageType::kResponse) {
        command.reset(new commands::SDLPResponse(message));
      } else {
        command.reset(new commands::RemoveProfileRequest(message));
      }
      break;
    }
    case mobile_apis::FunctionID::LoadProfileID: {
      if ((*message)[strings::params][strings::message_type]
          == MessageType::kResponse) {
        command.reset(new commands::SDLPResponse(message));
      } else {
        command.reset(new commands::LoadProfileRequest(message));
      }
      break;
    }
    case mobile_apis::FunctionID::UnloadProfileID: {
      if ((*message)[strings::params][strings::message_type]
          == MessageType::kResponse) {
        command.reset(new commands::SDLPResponse(message));
      } else {
        command.reset(new commands::UnloadProfileRequest(message));
      }
      break;
    }
    case mobile_apis::FunctionID::AppToProfileID: {
      if ((*message)[strings::params][strings::message_type]
          == MessageType::kResponse) {
        command.reset(new commands::SDLPResponse(message));
      } else {
        command.reset(new commands::AppToProfileRequest(message));
      }
      break;
    }
    case mobile_apis::FunctionID::AppStateChangedID: {
      if ((*message)[strings::params][strings::message_type]
          == MessageType::kResponse) {
        command.reset(new commands::SDLPResponse(message));
      } else {
        command.reset(new commands::AppStateChangedRequest(message));
      }
      break;
    }
    case mobile_apis::FunctionID::OnProfileClosedID: {
      command.reset(new commands::SDLPNotification(message));
      break;
    }
    case mobile_apis::FunctionID::OnSendProfileToAppMessageID: {
      command.reset(new commands::SDLPNotification(message));
      break;
    }
    default: {
      command.reset(new commands::GenericResponse(message));
      (*message)[strings::params][strings::function_id] =
          mobile_apis::FunctionID::GenericResponseID;
      break;
    }
  }

  return command;
}

}  // namespace application_manager
