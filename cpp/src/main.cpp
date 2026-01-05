#include <iostream>
#include <string>
#include "Application.h"

int main(int argc, char* argv[]) {
    try {
        std::string inputPath = "../shared/input.json";
        std::string outputPath = "../shared/output.json";

        std::cout << "CrossAI C++ Engine starting..." << std::endl;

        Application app(inputPath, outputPath);
        app.run();

        std::cout << "Recommendations generated successfully!" << std::endl;
        return 0;

    } catch (const std::exception& e) {
        std::cerr << "Error: " << e.what() << std::endl;
        return 1;
    }
}
