#ifndef SRC_COMPONENTS_APPLICATION_MANAGER_INCLUDE_APPLICATION_MANAGER_COMMANDS_PROFILE_MANAGER_RESPONSE_H_
#define SRC_COMPONENTS_APPLICATION_MANAGER_INCLUDE_APPLICATION_MANAGER_COMMANDS_PROFILE_MANAGER_RESPONSE_H_

#include "application_manager/commands/command_response_impl.h"
#include "utils/macro.h"

namespace application_manager
{

namespace commands
{

/**
 * @brief SDLPResponse command class
 **/
class SDLPResponse : public CommandResponseImpl
{
public:
    /**
     * @brief SDLPResponse class constructor
     *
     * @param message Outgoing SmartObject message
     **/
    explicit SDLPResponse(const MessageSharedPtr& message);

    /**
     * @brief SDLPResponse class destructor
     **/
    virtual ~SDLPResponse();

    /**
     * @brief Execute command
     **/
    virtual void Run();

private:
    DISALLOW_COPY_AND_ASSIGN(SDLPResponse);
};

}  // namespace commands
}  // namespace application_manager

#endif  // SRC_COMPONENTS_APPLICATION_MANAGER_INCLUDE_APPLICATION_MANAGER_COMMANDS_PROFILE_MANAGER_RESPONSE_H_
