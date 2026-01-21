cmake_minimum_required(VERSION 3.15)
project(CrossAI-Engine VERSION 1.0.0 LANGUAGES CXX)

set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_STANDARD_REQUIRED ON)

#  Include directories (make header files findable)
include_directories(${CMAKE_SOURCE_DIR}/include)

# Fetch JSON library
include(FetchContent)
FetchContent_Declare(json
    URL https://github.com/nlohmann/json/releases/download/v3.11.3/json.tar.xz
)
FetchContent_MakeAvailable(json)

# Fetch GoogleTest (NEW PART)
FetchContent_Declare(
  googletest
  URL https://github.com/google/googletest/archive/refs/tags/v1.14.0.zip
)
# For Windows: Prevent GTest from overriding parent overrides
set(gtest_force_shared_crt ON CACHE BOOL "" FORCE)
FetchContent_MakeAvailable(googletest)

# Main Executable
set(SOURCES
    src/main.cpp
    src/Application.cpp
    src/RuleBasedRecommender.cpp
    src/Item.cpp
    src/Genre.cpp
)

add_executable(crossai-engine ${SOURCES})
target_link_libraries(crossai-engine PRIVATE nlohmann_json::nlohmann_json)

# 5. Enable Tests
option(BUILD_TESTS "Build tests" ON)

if(BUILD_TESTS)
    enable_testing()
    add_subdirectory(tests)
endif()