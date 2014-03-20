#ifndef SRC_COMPONENTS_APPLICATION_MANAGER_INCLUDE_APPLICATION_MANAGER_COMMANDS_SDLP_NOTIFICATION_H_
#define SRC_COMPONENTS_APPLICATION_MANAGER_INCLUDE_APPLICATION_MANAGER_COMMANDS_SDLP_NOTIFICATION_H_

#include "application_manager/commands/command_notification_impl.h"
#include "utils/macro.h"

namespace application_manager
{

class Application;

namespace commands
{

/**
 * @brief SDLPNotification class is used to send notification
 * to mobile device that some button was pressed on HMI.
 **/
class SDLPNotification : public CommandNotificationImpl
{
public:
    /**
     * @brief SDLPNotification class constructor
     *
     * @param message Incoming SmartObject message
     **/
    explicit SDLPNotification(const MessageSharedPtr& message);

    /**
     * @brief SDLPNotification class destructor
     **/
    virtual ~SDLPNotification();

    /**
     * @brief Execute command
     **/
    virtual void Run();

private:
    DISALLOW_COPY_AND_ASSIGN(SDLPNotification);
};

}  // namespace commands
}  // namespace application_manager

#endif  // SRC_COMPONENTS_APPLICATION_MANAGER_INCLUDE_APPLICATION_MANAGER_COMMANDS_SDLP_NOTIFICATION_H_
