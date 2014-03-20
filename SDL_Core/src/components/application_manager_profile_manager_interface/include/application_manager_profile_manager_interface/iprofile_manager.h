#ifndef __PROFILE_MANAGER_IPROFILE_MANAGER_HPP
#define __PROFILE_MANAGER_IPROFILE_MANAGER_HPP

#include <string>
#include "application_manager_profile_manager_interface/profile_manager_result.h"
#include "interfaces/MOBILE_API.h"

namespace profile_manager
{
class IProfileManager
{
public:
    virtual ~IProfileManager() {};

    virtual ProfileManagerResult loadProfile(const int appId, const std::string & name) = 0;
    virtual ProfileManagerResult unloadProfile(const int appId, const std::string & name) = 0;
    virtual ProfileManagerResult sendAppToProfileMessage(const int appId,
            const std::string & profileName,
            const char * data,
            const int dataSize) = 0;
    virtual ProfileManagerResult addProfile(const int appId, const std::string & profileName,
                                            const char * profileBin,
                                            const int profileSize) = 0;
    virtual ProfileManagerResult removeProfile(const int appId, const std::string & profileName) = 0;
    virtual ProfileManagerResult appStateChanged(const int appId, const std::string & profileName,
            const mobile_apis::MobileAppState::eType state) = 0;
    virtual void onMobileAppExited(const int appId) = 0;

};

}

#endif //__PROFILE_MANAGER_IPROFILE_MANAGER_HPP