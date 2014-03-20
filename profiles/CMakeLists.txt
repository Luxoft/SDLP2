add_subdirectory(./TestEchoProfile)
add_subdirectory(./TestSendingMsgProfile)

# --- Generate profiles.config file


file(WRITE ${CMAKE_CURRENT_SOURCE_DIR}/profiles.config ${PROJECT_BINARY_DIR}/lib/profiles/)

file(COPY ${CMAKE_CURRENT_SOURCE_DIR}/profiles.config DESTINATION ${PROJECT_BINARY_DIR}/src/appMain/)
file(COPY ${CMAKE_CURRENT_SOURCE_DIR}/profiles.config DESTINATION ${PROJECT_BINARY_DIR}/bin/)

