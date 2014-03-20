include_directories (
  ./include
  ../../SDL_Core/src/components/utils/include/utils
  ../../SDL_Core/profileApi/
)

set (SOURCES
  ./src/TestSendingMsgProfile.cpp
)

set (HEADERS
  ./include/TestSendingMsgProfile.h
)

# set (CMAKE_LIBRARY_OUTPUT_DIRECTORY "${CMAKE_CURRENT_SOURCE_DIR}/../../SDL_Android/SmartDeviceLinkTester/assets/")

add_library("TestSendingMsgProfile" SHARED ${SOURCES} ${HEADERS})

install(TARGETS TestSendingMsgProfile LIBRARY DESTINATION lib/profiles/
                          ARCHIVE DESTINATION lib/profiles/
                          RUNTIME DESTINATION bin)
