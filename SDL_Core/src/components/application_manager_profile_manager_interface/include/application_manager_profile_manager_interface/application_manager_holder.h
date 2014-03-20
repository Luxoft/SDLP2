#ifndef __APPLICATION_MANAGER_HOLDER_H__
#define __APPLICATION_MANAGER_HOLDER_H__

#include "application_manager_profile_manager_interface/iapplication_manager.h"

namespace main_namespace
{
class LifeCycle;
}

namespace application_manager
{
class ApplicationManagerHolder
{
    friend class main_namespace::LifeCycle;
public:
    static IApplicationManager * getApplicationManagerInstance();
private:
    static void setApplicationManager(IApplicationManager * mgr);
};
}

#endif //__APPLICATION_MANAGER_HOLDER_H__