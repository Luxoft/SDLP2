#include "profile_manager/sdl_core_adapter.h"

#include "interfaces/MOBILE_API.h"
#include "application_manager/smart_object_keys.h"
#include "application_manager_profile_manager_interface/application_manager_holder.h"
#include "application_manager/application_manager_impl.h"

using namespace NsProfileManager;
using namespace application_manager;

void SDLCoreAdapter::sendMessageToApp(const int appId, std::string const& profileName, const char * data, const int dataSize)
{
    NsSmartDeviceLink::NsSmartObjects::SmartObject* notification = new NsSmartDeviceLink::NsSmartObjects::SmartObject;
    NsSmartDeviceLink::NsSmartObjects::SmartObject& message = *notification;
    message[strings::params][strings::function_id] = mobile_api::FunctionID::OnSendProfileToAppMessageID;
    message[strings::params][strings::message_type] = MessageType::kNotification;
    message[strings::params][strings::connection_key] = appId;
    message[strings::msg_params][strings::profile_name] = profileName;
    std::vector<unsigned char> binaryData(data, data + dataSize);
    message[strings::params][strings::binary_data] = NsSmartDeviceLink::NsSmartObjects::SmartObject(binaryData);
    ApplicationManagerHolder::getApplicationManagerInstance()->ManageMobileCommand(notification);
}

void SDLCoreAdapter::sendProfileClosedNotification(const int appId, std::string const& profileName)
{
    NsSmartDeviceLink::NsSmartObjects::SmartObject* notification = new NsSmartDeviceLink::NsSmartObjects::SmartObject;
    NsSmartDeviceLink::NsSmartObjects::SmartObject& message = *notification;
    message[strings::params][strings::function_id] = mobile_api::FunctionID::OnProfileClosedID;
    message[strings::params][strings::message_type] = MessageType::kNotification;
    message[strings::params][strings::connection_key] = appId;
    message[strings::msg_params][strings::profile_name] = profileName;
    ApplicationManagerHolder::getApplicationManagerInstance()->ManageMobileCommand(notification);
}

