#ifndef SRC_COMPONENTS_APPLICATION_MANAGER_INCLUDE_APPLICATION_MANAGER_COMMANDS_UNLOAD_PROFILE_REQUEST_H_
#define SRC_COMPONENTS_APPLICATION_MANAGER_INCLUDE_APPLICATION_MANAGER_COMMANDS_UNLOAD_PROFILE_REQUEST_H_

#include "application_manager/commands/command_request_impl.h"
#include "utils/macro.h"

namespace application_manager
{

class Application;

namespace commands
{

/**
 * @brief UnloadProfileRequest command class
 **/
class UnloadProfileRequest : public CommandRequestImpl
{
public:
    /**
     * @brief  class constructor
     *
     * @param message Incoming SmartObject message
     **/
    explicit UnloadProfileRequest(const MessageSharedPtr& message);

    /**
     * @brief UnloadProfileRequest class destructor
     **/
    virtual ~UnloadProfileRequest();

    /**
     * @brief Execute command
     **/
    virtual void Run();

private:

    DISALLOW_COPY_AND_ASSIGN(UnloadProfileRequest);
};

}  // namespace commands
}  // namespace application_manager

#endif  // SRC_COMPONENTS_APPLICATION_MANAGER_INCLUDE_APPLICATION_MANAGER_COMMANDS_UNLOAD_PROFILE_REQUEST_H_
