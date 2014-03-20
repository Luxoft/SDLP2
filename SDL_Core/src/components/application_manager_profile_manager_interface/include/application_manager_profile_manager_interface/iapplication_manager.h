#ifndef __IAPPLICATION_MANAGER_H__
#define __IAPPLICATION_MANAGER_H__

#include "smart_objects/smart_object.h"

namespace application_manager
{
class IApplicationManager
{
public:
    virtual ~IApplicationManager() {}
    virtual bool ManageMobileCommand(const utils::SharedPtr<NsSmartDeviceLink::NsSmartObjects::SmartObject>& message) = 0;
};
}

#endif //__IAPPLICATION_MANAGER_H__