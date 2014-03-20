#ifndef SRC_COMPONENTS_APPLICATION_MANAGER_INCLUDE_APPLICATION_MANAGER_COMMANDS_APP_TO_PROFILE_REQUEST_H_
#define SRC_COMPONENTS_APPLICATION_MANAGER_INCLUDE_APPLICATION_MANAGER_COMMANDS_APP_TO_PROFILE_REQUEST_H_

#include "application_manager/commands/command_request_impl.h"
#include "utils/macro.h"

namespace application_manager
{

class Application;

namespace commands
{

/**
 * @brief AppToProfileRequest command class
 **/
class AppToProfileRequest : public CommandRequestImpl
{
public:
    /**
     * @brief  class constructor
     *
     * @param message Incoming SmartObject message
     **/
    explicit AppToProfileRequest(const MessageSharedPtr& message);

    /**
     * @brief AppToProfileRequest class destructor
     **/
    virtual ~AppToProfileRequest();

    /**
     * @brief Execute command
     **/
    virtual void Run();

private:

    DISALLOW_COPY_AND_ASSIGN(AppToProfileRequest);
};

}  // namespace commands
}  // namespace application_manager

#endif  // SRC_COMPONENTS_APPLICATION_MANAGER_INCLUDE_APPLICATION_MANAGER_COMMANDS_APP_TO_PROFILE_REQUEST_H_
