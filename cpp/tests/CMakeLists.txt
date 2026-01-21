# Define the executable
# RENAME: "run_tests" -> "unit_tests" to avoid conflict with VS "RUN_TESTS" command
add_executable(unit_tests 
    tests.cpp
    ../src/Item.cpp 
    ../src/Genre.cpp 
    ../src/RuleBasedRecommender.cpp
)

target_include_directories(unit_tests PRIVATE ../include)

target_link_libraries(unit_tests PRIVATE 
    GTest::gtest_main 
    nlohmann_json::nlohmann_json
)

add_test(NAME GoogleTests COMMAND $<TARGET_FILE:unit_tests>)