cmake_minimum_required(VERSION 3.15)
project(CrossAI-Engine VERSION 1.0.0 LANGUAGES CXX)

set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_STANDARD_REQUIRED ON)

# Include directories
include_directories(${CMAKE_SOURCE_DIR}/include)

# Add JSON library (nlohmann/json - header-only)
include(FetchContent)
FetchContent_Declare(json
    URL https://github.com/nlohmann/json/releases/download/v3.11.3/json.tar.xz
)
FetchContent_MakeAvailable(json)

# Source files
set(SOURCES
    src/main.cpp
    src/Application.cpp
    src/RuleBasedRecommender.cpp
    src/Item.cpp
)

# Executable
add_executable(crossai-engine ${SOURCES})
target_link_libraries(crossai-engine PRIVATE nlohmann_json::nlohmann_json)

# Tests (optional for now)
option(BUILD_TESTS "Build tests" OFF)
if(BUILD_TESTS)
    enable_testing()
    add_subdirectory(tests)
endif()