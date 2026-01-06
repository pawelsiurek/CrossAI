#pragma once
#include <string>
#include <vector>
#include "Item.h"
#include "Recommender.h"
#include <memory>
#include <thread>

class Application {
private:
    std::string inputPath;
    std::string outputPath;
    std::unique_ptr<Recommender> recommender;
    
    // NEW: Python ML integration methods
    void prepareMLInput(const std::vector<std::string>& preferredGenres);
    void copyMLOutputToOutput();
    void readMLOutput();

public:
    Application(const std::string& inputPath, const std::string& outputPath);
    
    void run();
    void callPythonMLAsync();
    void handleError(const std::exception& e);
};