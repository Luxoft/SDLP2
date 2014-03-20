include_directories (
  ./include
  ./include/utils/zmq/
  ./include/utils/
  ../config_profile/include
  ./../../thirdPartyLibs/jsoncpp/include
)

set (SOURCES
    ./src/file_system.cc
    ./src/threads/posix_thread.cc
    ./src/synchronisation_primitives_posix.cc
    ./src/timer_linux.cc
    ./src/date_time.cc
    ./src/signals_linux.cc
    ./src/base_64_coder.cc
    ./src/uuid_store.cc
    ./src/work_with_os.cc
    ./src/logger.cc
)

add_library("Utils" ${SOURCES})
target_link_libraries ("Utils" log4cxx rt)

set (ZMQ_SOURCES
    ./src/zmq/sender_zmq_socket.cc
    ./src/zmq/receiver_zmq_socket.cc
    ./src/zmq/zmq_socket.cc
)

add_library("ZmqSocket" ${ZMQ_SOURCES})
target_link_libraries ("ZmqSocket" zmq)
