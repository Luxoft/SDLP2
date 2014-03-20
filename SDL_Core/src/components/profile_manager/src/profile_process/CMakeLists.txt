set(LIBRARIES
  AppManHmiProtocol
  ProfileManagerIPC
  ZmqSocket
  Utils
  jsoncpp
  log4cplus
  zmq
  pthread
  dl
)

set( CMAKE_VERBOSE_MAKEFILE on )

include_directories (
  ./../../include/
  ./../../../../../
  ./../../../utils/include/
  ./../../../utils/include/utils/
  ./../../../utils/include/utils/zmq/
  ./../../../thirdPartyLibs/jsoncpp/include/
)

set (SOURCES
  ./profile_process.cc
  ./main.cc
)

add_executable("ProfileProcess" ${SOURCES})
target_link_libraries("ProfileProcess" ${LIBRARIES})

file(WRITE ${CMAKE_BINARY_DIR}/bin/profile_process_link "${CMAKE_BINARY_DIR}/bin/ProfileProcess")
file(WRITE ${CMAKE_BINARY_DIR}/src/appMain/profile_process_link "${CMAKE_CURRENT_BINARY_DIR}/ProfileProcess")
install (TARGETS ProfileProcess DESTINATION bin)
