find_package(Gstreamer-1.0 REQUIRED)
find_package(Glib-2.0 REQUIRED)

# required to find 'glibconfig.h'
find_package(PkgConfig)
pkg_check_modules(GLIB2 REQUIRED glib-2.0)
add_definitions(${GLIB2_CFLAGS})

include_directories (
  ./include
  ../utils/include/
  ../interfaces
  ../application_manager/include/
  
  ../hmi_message_handler/include
  ../mobile_message_handler/include
  ../connection_handler/include/
  ../transport_manager/include/
  ../protocol_handler/include/
  ../request_watchdog/include/
  ../smart_objects/include/
  ../formatters/include/
  ../application_manager_profile_manager_interface/include
  
    ../../thirdPartyLibs/logger/include/
  ../../thirdPartyLibs/MessageBroker/include/
  ${GSTREAMER_gst_INCLUDE_DIR}
  ${GLIB_glib_2_INCLUDE_DIR}
  ${CMAKE_BINARY_DIR}/src/components/
)

set(LIBRARIES
  ${GSTREAMER_gstreamer_LIBRARY}
)

set (SOURCES
    ./src/audio_manager_impl.cc
    ./src/from_mic_to_file_recorder_thread.cc
    ./src/a2dp_source_player_thread.cc
    ./src/audio_stream_sender_thread.cc
)

add_library("AudioManager" ${SOURCES} )
target_link_libraries ("AudioManager" Utils)
target_link_libraries ("AudioManager" log4cxx)
target_link_libraries ("AudioManager" pulse-simple)
target_link_libraries ("AudioManager" ${LIBRARIES})
