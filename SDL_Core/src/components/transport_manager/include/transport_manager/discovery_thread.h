#ifndef SRC_COMPONENTS_TRANSPORT_MANAGER_INCLUDE_TRANSPORT_MANAGER_DISCOVERY_THREAD_H__
#define SRC_COMPONENTS_TRANSPORT_MANAGER_INCLUDE_TRANSPORT_MANAGER_DISCOVERY_THREAD_H__

#include <pthread.h>

namespace transport_manager
{
class DiscoveryThread
{
    pthread_t mBroadcastThread;
    volatile bool mShutdownFlag;
public:
    DiscoveryThread();
    ~DiscoveryThread();
private:
    static void * broadcastThread(void * Data);
    void broadcastLoop();
};
}

#endif //SRC_COMPONENTS_TRANSPORT_MANAGER_INCLUDE_TRANSPORT_MANAGER_DISCOVERY_THREAD_H__