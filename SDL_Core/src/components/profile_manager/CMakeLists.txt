# --- ProfileProcess
add_subdirectory(./src/profile_process)

include_directories (
  ./include
  ./ProfileManagerUtils/include/
  ./ApplicationManagerHmiProtocol/include/
  ./../utils/include/
  ./../utils/include/utils
  ./../utils/include/utils/zmq
  ./../../thirdPartyLibs/jsoncpp/include/
  ./../application_manager/include/
  ./../hmi_message_handler/include/
  ./../mobile_message_handler/include/
  ./../connection_handler/include/
  ./../transport_manager/include/
  ./../protocol_handler/include/
  ./../smart_objects/include/
  ./../request_watchdog/include/
  ./../audio_manager/include/  
  ./../formatters/include/
  ./../application_manager_profile_manager_interface/include
  ./../../../
  ${GSTREAMER_gst_INCLUDE_DIR}
  ${GLIB_glib_2_INCLUDE_DIR}
  /usr/lib/i386-linux-gnu/glib-2.0/include
  /usr/lib/x86_64-linux-gnu/glib-2.0/include/
  ${CMAKE_BINARY_DIR}/src/components/
)

set (SOURCES
  ./src/profile_instance_manager.cc
  ./src/profile_manager.cc
  ./src/active_profiles_register.cc
  ./src/profile_connections_register.cc
  ./src/profile_libraries_manager.cc
  ./src/application_manager_hmi_communicator.cc
  ./src/sdl_core_adapter.cc
  ./src/profile_sockets_register.cc
)

add_library("ProfileManager" ${SOURCES})

add_library("AppManHmiProtocol" ./src/appman_hmi_protocol/profiles_apps_message.cc)

set (IPC_SOURCES
  ./src/ipc/pm_sender_zmq_socket.cc
  ./src/ipc/pm_receiver_zmq_socket.cc
)

add_library("ProfileManagerIPC" ${IPC_SOURCES})