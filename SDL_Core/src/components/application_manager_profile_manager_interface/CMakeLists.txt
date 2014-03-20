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
  ${CMAKE_BINARY_DIR}/src/components/
)

set (SOURCES
  ./src/profile_manager_holder.cc
  ./src/application_manager_holder.cc
)

add_library("AppManPMInterface" ${SOURCES} )
