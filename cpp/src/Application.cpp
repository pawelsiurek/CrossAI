#include "Application.h"
#include "RuleBasedRecommender.h"
#include <nlohmann/json.hpp>
#include <fstream>
#include <iostream>
#include <cstdlib>
#include "Genre.h"
#include <thread>
#include <chrono>

using json = nlohmann::json;

Application::Application(const std::string& inputPath, const std::string& outputPath)
    : inputPath(inputPath), outputPath(outputPath) {
    recommender = std::make_unique<RuleBasedRecommender>();
}

void Application::run() {
    try {
        std::cout << "Reading input from: " << inputPath << std::endl;
        
        // 1. Read input.json
        std::ifstream inputFile(inputPath);
        if (!inputFile.is_open()) {
            throw std::runtime_error("Could not open input file: " + inputPath);
        }
        
        json inputData;
        inputFile >> inputData;
        inputFile.close();
        
        // 2. Parse user preferences
        std::vector<std::string> preferredGenres;
        if (inputData.contains("user") && inputData["user"].contains("preferredGenres")) {
            for (const auto& genre : inputData["user"]["preferredGenres"]) {
                preferredGenres.push_back(genre.get<std::string>());
            }
        }
        
        std::string userName = inputData["user"]["name"].get<std::string>();
        int userAge = inputData["user"]["age"].get<int>();

        std::cout << "User: " << userName << ", Age: " << userAge << std::endl;

        std::cout << "User preferred genres: ";
        for (const auto& genre : preferredGenres) {
            std::cout << genre << " ";
        }
        std::cout << std::endl;
        
        // 5. Prepare input for Python ML
        prepareMLInput(preferredGenres);
        
        // 6. Call Python ML asynchronously
        callPythonMLAsync();
        
        // 7. Copy ML recommendations to output.json (ONLY ML, not rule-based!)
        copyMLOutputToOutput();
        
        // 8. Optional: Display ML results in console
        readMLOutput();
        
        std::cout << "Recommendations generated successfully!\n";
        
    } catch (const std::exception& e) {
        handleError(e);
        throw;
    }
}

void Application::prepareMLInput(const std::vector<std::string>& preferredGenres) {
    std::cout << "Preparing ML input for Python...\n";
    
    // Create JSON input file for Python
    json mlInput;
    mlInput["preferredGenres"] = preferredGenres;
    
    std::ofstream file("../../shared/ml_input.json");
    if (!file.is_open()) {
        std::cerr << "Could not create ml_input.json\n";
        return;
    }
    
    file << mlInput.dump(2);  // Pretty print with 2 spaces
    file.close();
    
    std::cout << "ML input prepared with genres: ";
    for (const auto& g : preferredGenres) {
        std::cout << g << " ";
    }
    std::cout << "\n";
}

void Application::callPythonMLAsync() {
    std::cout << "Launching Python ML in separate thread...\n";
    
    std::thread pythonThread([this]() {
        std::cout << "[THREAD] Python ML thread started (ID: " 
                  << std::this_thread::get_id() << ")\n";
        
        // Call Python ML script
        std::string pythonCommand = "python ..\\..\\python\\recommender\\model.py";
        std::cout << "[THREAD] Executing: " << pythonCommand << "\n";
        
        int result = system(pythonCommand.c_str());
        
        if (result == 0) {
            std::cout << "[THREAD] Python ML processing complete\n";
        } else {
            std::cerr << "[THREAD] Python ML failed with code: " << result << "\n";
            std::cerr << "[THREAD] Check that Python and dependencies are installed\n";
        }
    });
    
    std::cout << "[MAIN] Main thread continues (ID: " 
              << std::this_thread::get_id() << ")\n";
    std::cout << "[MAIN] Doing other work while Python runs...\n";
    
    // Simulate some work
    std::this_thread::sleep_for(std::chrono::milliseconds(100));
    
    std::cout << "[MAIN] Waiting for Python thread to finish...\n";
    pythonThread.join();
    
    std::cout << "[MAIN] Python thread joined - all work complete\n";
    std::cout << "=== Parallel Programming Complete ===\n\n";
}

void Application::copyMLOutputToOutput() {
    std::cout << "Copying ML recommendations to output.json...\n";
    
    // Read ml_output.json
    std::ifstream mlFile("../../shared/ml_output.json");
    if (!mlFile.is_open()) {
        std::cerr << "ERROR: Could not read ml_output.json\n";
        std::cerr << "Python ML may have failed. Check logs above.\n";
        
        // Create empty output as fallback
        json emptyOutput;
        emptyOutput["recommendations"] = json::array();
        emptyOutput["error"] = "Python ML failed to generate recommendations";
        
        std::ofstream outputFile(outputPath);
        outputFile << emptyOutput.dump(4);
        outputFile.close();
        return;
    }
    
    json mlData;
    mlFile >> mlData;
    mlFile.close();
    
    // Convert ml_recommendations to output.json format
    json outputData;
    outputData["recommendations"] = json::array();
    
    if (mlData.contains("ml_recommendations")) {
        for (const auto& movie : mlData["ml_recommendations"]) {
            json movieJson;
            movieJson["id"] = movie["id"];
            movieJson["title"] = movie["title"];
            
            // Handle genres array
            if (movie.contains("genres")) {
                movieJson["genres"] = movie["genres"];
            }
            
            // Handle rating (Python uses vote_average)
            if (movie.contains("vote_average")) {
                movieJson["rating"] = movie["vote_average"];
            } else if (movie.contains("rating")) {
                movieJson["rating"] = movie["rating"];
            } else {
                movieJson["rating"] = 0.0;
            }
            
            // Include ML score for reference
            if (movie.contains("ml_score")) {
                movieJson["ml_score"] = movie["ml_score"];
            }
            
            // Include vote_count and popularity if available
            if (movie.contains("vote_count")) {
                movieJson["vote_count"] = movie["vote_count"];
            }
            if (movie.contains("popularity")) {
                movieJson["popularity"] = movie["popularity"];
            }
            
            outputData["recommendations"].push_back(movieJson);
        }
        
        std::cout << "Copied " << outputData["recommendations"].size() 
                  << " ML recommendations to output.json\n";
    } else {
        std::cerr << "WARNING: ml_output.json does not contain 'ml_recommendations' field\n";
        outputData["recommendations"] = json::array();
        outputData["error"] = "Invalid ML output format";
    }
    
    // Write to output.json (overwrites any previous content)
    std::ofstream outputFile(outputPath);
    if (!outputFile.is_open()) {
        throw std::runtime_error("Could not open output file: " + outputPath);
    }
    
    outputFile << outputData.dump(4);  // Pretty print with 4 spaces
    outputFile.close();
    
    std::cout << "Results written to: " << outputPath << std::endl;
}

void Application::readMLOutput() {
    std::cout << "\nReading Python ML recommendations...\n";
    
    std::ifstream file("../../shared/ml_output.json");
    if (!file.is_open()) {
        std::cerr << "Could not open ml_output.json (Python may have failed)\n";
        return;
    }
    
    json mlOutput;
    file >> mlOutput;
    file.close();
    
    if (!mlOutput.contains("ml_recommendations")) {
        std::cerr << "Invalid ml_output.json format\n";
        return;
    }
    
    auto mlRecs = mlOutput["ml_recommendations"];
    std::cout << "=== Python ML Recommendations (" << mlOutput["count"] << " total) ===\n";
    
    int count = 0;
    for (const auto& movie : mlRecs) {
        count++;
        std::cout << count << ". \"" << movie["title"].get<std::string>() << "\"";
        
        if (movie.contains("ml_score")) {
            std::cout << " (Score: " << movie["ml_score"] << ")";
        }
        
        if (movie.contains("vote_average")) {
            std::cout << " [Rating: " << movie["vote_average"] << "/10]";
        }
        
        std::cout << "\n";
        
        if (count >= 5) break;  // Show top 5
    }
    
    if (mlRecs.size() > 5) {
        std::cout << "... and " << (mlRecs.size() - 5) << " more\n";
    }
    
    std::cout << "=== End of ML Recommendations ===\n\n";
}

void Application::handleError(const std::exception& e) {
    std::cerr << "Application Error: " << e.what() << std::endl;
}