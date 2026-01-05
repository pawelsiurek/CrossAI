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

public:
    Application(const std::string& inputPath, const std::string& outputPath);
    
    void run();
    void loadItems(const std::string& path);
    void writeIntermediateData(const std::string& path);
    void callPythonML();
    void callPythonMLAsync();
    void handleError(const std::exception& e);
};