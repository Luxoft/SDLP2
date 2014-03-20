#ifndef __PROFILE_MANAGER_SDL_CORE_ADAPTER_H__
#define __PROFILE_MANAGER_SDL_CORE_ADAPTER_H__

#include "profile_manager/isdl_core_adapter.h"

namespace NsProfileManager
{

class SDLCoreAdapter : public ISDLCoreAdapter
{
public:
    virtual ~SDLCoreAdapter() {}

    virtual void sendMessageToApp(const int appId, std::string const& profileName, const char * data, const int dataSize);
    virtual void sendProfileClosedNotification(const int appId, std::string const& profileName);

};
}

#endif //__PROFILE_MANAGER_SDL_CORE_ADAPTER_H__