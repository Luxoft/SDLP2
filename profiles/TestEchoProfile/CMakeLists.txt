include_directories (
  ./include
  ../../SDL_Core/profileApi
  ../../SDL_Core/src/components/utils/include/utils
)

set (SOURCES
  ./src/TestEchoProfile.cpp
)

set (HEADERS
  ./include/TestEchoProfile.h
)

# set (CMAKE_LIBRARY_OUTPUT_DIRECTORY "${CMAKE_CURRENT_SOURCE_DIR}/../../SDL_Android/SmartDeviceLinkTester/assets/")

add_library("TestEchoProfile" SHARED ${SOURCES} ${HEADERS})

install(TARGETS TestEchoProfile LIBRARY DESTINATION lib/profiles/
                          ARCHIVE DESTINATION lib/profiles/
                          RUNTIME DESTINATION bin)
