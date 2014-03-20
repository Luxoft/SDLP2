#ifndef SRC_COMPONENTS_APPLICATION_MANAGER_INCLUDE_APPLICATION_MANAGER_COMMANDS_LOAD_PROFILE_REQUEST_H_
#define SRC_COMPONENTS_APPLICATION_MANAGER_INCLUDE_APPLICATION_MANAGER_COMMANDS_LOAD_PROFILE_REQUEST_H_

#include "application_manager/commands/command_request_impl.h"
#include "utils/macro.h"

namespace application_manager
{

class Application;

namespace commands
{

/**
 * @brief LoadProfileRequest command class
 **/
class LoadProfileRequest : public CommandRequestImpl
{
public:
    /**
     * @brief LoadProfileRequest class constructor
     *
     * @param message Incoming SmartObject message
     **/
    explicit LoadProfileRequest(const MessageSharedPtr& message);

    /**
     * @brief LoadProfileRequest class destructor
     **/
    virtual ~LoadProfileRequest();

    /**
     * @brief Execute command
     **/
    virtual void Run();

private:

    DISALLOW_COPY_AND_ASSIGN(LoadProfileRequest);
};

}  // namespace commands
}  // namespace application_manager

#endif  // SRC_COMPONENTS_APPLICATION_MANAGER_INCLUDE_APPLICATION_MANAGER_COMMANDS_LOAD_PROFILE_REQUEST_H_
