#ifndef SRC_COMPONENTS_APPLICATION_MANAGER_INCLUDE_APPLICATION_MANAGER_COMMANDS_REMOVE_PROFILE_REQUEST_H_
#define SRC_COMPONENTS_APPLICATION_MANAGER_INCLUDE_APPLICATION_MANAGER_COMMANDS_REMOVE_PROFILE_REQUEST_H_

#include "application_manager/commands/command_request_impl.h"
#include "utils/macro.h"

namespace application_manager
{

class Application;

namespace commands
{

/**
 * @brief RemoveProfileRequest command class
 **/
class RemoveProfileRequest : public CommandRequestImpl
{
public:
    /**
     * @brief RemoveProfileRequest class constructor
     *
     * @param message Incoming SmartObject message
     **/
    explicit RemoveProfileRequest(const MessageSharedPtr& message);

    /**
     * @brief RemoveProfileRequest class destructor
     **/
    virtual ~RemoveProfileRequest();

    /**
     * @brief Execute command
     **/
    virtual void Run();

private:

    DISALLOW_COPY_AND_ASSIGN(RemoveProfileRequest);
};

}  // namespace commands
}  // namespace application_manager

#endif  // SRC_COMPONENTS_APPLICATION_MANAGER_INCLUDE_APPLICATION_MANAGER_COMMANDS_REMOVE_PROFILE_REQUEST_H_
