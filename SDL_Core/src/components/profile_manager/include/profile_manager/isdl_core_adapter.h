#ifndef __PROFILE_MANAGER_I_SDL_CORE_ADAPTER_HPP__
#define __PROFILE_MANAGER_I_SDL_CORE_ADAPTER_HPP__

#include <string>

namespace NsProfileManager
{
class ISDLCoreAdapter
{
public:
    virtual ~ISDLCoreAdapter() {}

    virtual void sendMessageToApp(const int appId, std::string const& profileName, const char * data, const int dataSize) = 0;
    virtual void sendProfileClosedNotification(const int appId, std::string const& profileName) = 0;

};
}

#endif //__PROFILE_MANAGER_I_SDL_CORE_ADAPTER_HPP__