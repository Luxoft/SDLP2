#ifndef __PROFILE_MANAGER_I_PROFILE_MANAGER_PROXY_HPP__
#define __PROFILE_MANAGER_I_PROFILE_MANAGER_PROXY_HPP__

#include <string>

namespace NsProfileManager
{
/**
 * @brief The IProfileManagerProxy class is the interface of Profile Manager for Profile instance
 */
class IProfileManagerProxy
{
public:
    /**
     * @brief sendProfileToAppMessage Send message from profile to the mobile application
     * @param appId Application id
     * @param data Pointer to the data array
     * @param dataSize Data array size
     */
    virtual void sendProfileToAppMessage(const int appId,
                                         const char * data,
                                         const int dataSize) = 0;

    /**
     * @brief broadcastProfileToAppMessage Send message from profile to all mobile applications
     * that have ever communicated with this profile
     * @param data Pointer to the data array
     * @param dataSize Data array size
     */
    virtual void broadcastProfileToAppMessage(const char * data,
            const int dataSize) = 0;

    virtual void profileClosed() = 0;

    virtual void sendHmiRequest(std::string const& applicationName, const char * data, const int dataSize) = 0;

    virtual ~IProfileManagerProxy() {}
};
}

#endif //__PROFILE_MANAGER_I_PROFILE_MANAGER_PROXY_HPP__