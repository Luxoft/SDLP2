#ifndef __PROFILE_MANAGER_PROFILE_MANAGER_HOLDER_HPP__
#define __PROFILE_MANAGER_PROFILE_MANAGER_HOLDER_HPP__

#include "application_manager_profile_manager_interface/iprofile_manager.h"

namespace main_namespace
{
class LifeCycle;
}

namespace profile_manager
{
class ProfileManagerHolder
{
    friend class main_namespace::LifeCycle;
public:
    static IProfileManager * getProfileManagerInstance();
private:
    static void setProfileManager(IProfileManager * mgr);
};
}

#endif //__PROFILE_MANAGER_PROFILE_MANAGER_HOLDER_HPP__