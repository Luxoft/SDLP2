#ifndef SRC_COMPONENTS_APPLICATION_MANAGER_INCLUDE_APPLICATION_MANAGER_COMMANDS_APP_STATE_CHANGED_REQUEST_H
#define SRC_COMPONENTS_APPLICATION_MANAGER_INCLUDE_APPLICATION_MANAGER_COMMANDS_APP_STATE_CHANGED_REQUEST_H

#include "application_manager/commands/command_request_impl.h"
#include "utils/macro.h"

namespace application_manager
{

class Application;

namespace commands
{

/**
 * @brief AppStateChangedRequest command class
 **/
class AppStateChangedRequest : public CommandRequestImpl
{
public:
    /**
     * @brief AppStateChangedRequest class constructor
     *
     * @param message Incoming SmartObject message
     **/
    explicit AppStateChangedRequest(const MessageSharedPtr& message);

    /**
     * @brief AppStateChangedRequest class destructor
     **/
    virtual ~AppStateChangedRequest();

    /**
     * @brief Execute command
     **/
    virtual void Run();

private:

    DISALLOW_COPY_AND_ASSIGN(AppStateChangedRequest);
};

}  // namespace commands
}  // namespace application_manager

#endif  // SRC_COMPONENTS_APPLICATION_MANAGER_INCLUDE_APPLICATION_MANAGER_COMMANDS_APP_STATE_CHANGED_REQUEST_H
