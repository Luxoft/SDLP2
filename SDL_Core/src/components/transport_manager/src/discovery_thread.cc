
#include <memory.h>
#include <arpa/inet.h>
#include <netinet/in.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <fcntl.h>

#include "utils/logger.h"
#include "utils/uuid_store.h"
#include "transport_manager/discovery_thread.h"
#include "transport_manager/common.h"


#define BRD_MESSAGE_TEXT "SMARTDEVICELINK"
#define NAME_LENGTH 32
#define UDP_BROADCAST_PORT 45678


struct tBrdMessage
{
    char handshake[sizeof(BRD_MESSAGE_TEXT)];
    int32_t boundTcpPortNumber;
    char name[NAME_LENGTH];
    char uuid[UUID_LENGTH];
    char senderIp[INET_ADDRSTRLEN];
} __attribute__((__packed__));

using namespace transport_manager;

static log4cxx::LoggerPtr logger = log4cxx::LoggerPtr(log4cxx::Logger::getLogger("DiscoveryThread"));
DiscoveryThread::DiscoveryThread()
{
    LOG4CXX_TRACE_METHOD(logger, __PRETTY_FUNCTION__);
    mShutdownFlag = false;
    pthread_create(&mBroadcastThread, 0, transport_manager::DiscoveryThread::broadcastThread, (void*)this);
}

DiscoveryThread::~DiscoveryThread()
{
    LOG4CXX_TRACE_METHOD(logger, __PRETTY_FUNCTION__);
    mShutdownFlag = true;
    pthread_join(mBroadcastThread, 0);
}

void * DiscoveryThread::broadcastThread(void * data)
{
    LOG4CXX_TRACE_METHOD(logger, __PRETTY_FUNCTION__);
    ((DiscoveryThread*)data)->broadcastLoop();
    return NULL;
}

void DiscoveryThread::broadcastLoop()
{
    LOG4CXX_TRACE_METHOD(logger, __PRETTY_FUNCTION__);
    struct sockaddr_in srcAddress;
    int broadcastSock = socket(AF_INET, SOCK_DGRAM, IPPROTO_UDP);
    tBrdMessage mMessage;
    sockaddr_in mBroadcastAddr;
    memcpy(mMessage.handshake, BRD_MESSAGE_TEXT, sizeof(mMessage.handshake));
    memset(mMessage.name, 0, sizeof(mMessage.name));
    gethostname(mMessage.name, sizeof(mMessage.name));//Name is generated from Hostname
    mMessage.name[sizeof(mMessage.name) - 1] = '\0';
    LOG4CXX_INFO(logger, "Got hostname = " + std::string(mMessage.name) );

    {
        UUIDStore myUUIDStore;
        std::string uuid = myUUIDStore.getByName(mMessage.name);
        LOG4CXX_INFO(logger, "Got uuid = " + uuid);
        memcpy(mMessage.uuid, uuid.c_str(), UUID_LENGTH);
        mMessage.uuid[UUID_LENGTH - 1] = '\0';
    }

    mMessage.boundTcpPortNumber = default_port;

    int optval = 1;
    setsockopt(broadcastSock, SOL_SOCKET, SO_REUSEADDR, &optval, sizeof(optval));
    setsockopt(broadcastSock, SOL_SOCKET, SO_BROADCAST, &optval, sizeof(optval));
    
    timeval tv;
    tv.tv_sec = 1;
    tv.tv_usec = 0;
    setsockopt(broadcastSock, SOL_SOCKET, SO_RCVTIMEO, &tv, sizeof(tv));


    srcAddress.sin_port = htons(UDP_BROADCAST_PORT);
    srcAddress.sin_family = AF_INET;
    srcAddress.sin_addr.s_addr = INADDR_ANY;
    memset(srcAddress.sin_zero, '\0', sizeof(srcAddress.sin_zero));
    bind(broadcastSock, (struct sockaddr*)&srcAddress, sizeof(srcAddress));

    while (!mShutdownFlag)
    {
        socklen_t srcAddressSize = sizeof(srcAddress);
        char msgBuffer[sizeof(mMessage) + 1];
        ssize_t packetLength = 0;
        packetLength = recvfrom(broadcastSock, msgBuffer, sizeof(msgBuffer), 0,
                                (sockaddr*)&srcAddress, &srcAddressSize);
        if (mShutdownFlag)
        {
            break;
        }
        if (packetLength == (strlen(BRD_MESSAGE_TEXT) + 1))
        {
            LOG4CXX_INFO(logger, "In listen and reply loop: packetLength = " << packetLength);
            msgBuffer[packetLength] = '\0';
            LOG4CXX_INFO(logger, "In listen and reply loop: buffer = " << msgBuffer);
            if (memcmp(msgBuffer, BRD_MESSAGE_TEXT, strlen(BRD_MESSAGE_TEXT)) == 0)
            {
                memset(mMessage.senderIp, 0, INET_ADDRSTRLEN);
                snprintf(mMessage.senderIp, INET_ADDRSTRLEN, "%s", inet_ntoa(srcAddress.sin_addr));
                LOG4CXX_INFO(logger, "In listen and reply loop: reply to = " << mMessage.senderIp);
                LOG4CXX_INFO(logger, mMessage.senderIp);
                sendto(broadcastSock, &mMessage, sizeof(mMessage), 0, (struct sockaddr *)&srcAddress,
                       srcAddressSize);
            }
        }
    }
}