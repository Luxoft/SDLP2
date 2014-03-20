#ifndef SRC_COMPONENTS_APPLICATION_MANAGER_INCLUDE_APPLICATION_MANAGER_COMMANDS_ADD_PROFILE_REQUEST_H_
#define SRC_COMPONENTS_APPLICATION_MANAGER_INCLUDE_APPLICATION_MANAGER_COMMANDS_ADD_PROFILE_REQUEST_H_

#include "application_manager/commands/command_request_impl.h"
#include "utils/macro.h"

namespace application_manager
{

class Application;

namespace commands
{

/**
 * @brief AddProfileRequest command class
 **/
class AddProfileRequest : public CommandRequestImpl
{
public:
    /**
     * @brief AddProfileRequest class constructor
     *
     * @param message Incoming SmartObject message
     **/
    explicit AddProfileRequest(const MessageSharedPtr& message);

    /**
     * @brief AddProfileRequest class destructor
     **/
    virtual ~AddProfileRequest();

    /**
     * @brief Execute command
     **/
    virtual void Run();

private:

    DISALLOW_COPY_AND_ASSIGN(AddProfileRequest);
};

}  // namespace commands
}  // namespace application_manager

#endif  // SRC_COMPONENTS_APPLICATION_MANAGER_INCLUDE_APPLICATION_MANAGER_COMMANDS_ADD_PROFILE_REQUEST_H_
