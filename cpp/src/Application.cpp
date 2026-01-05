#include "Application.h"
#include "RuleBasedRecommender.h"
#include <nlohmann/json.hpp>
#include <fstream>
#include <iostream>
#include <cstdlib>

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
        
        // 3. Load movie items (for now, create dummy data)
        // TODO: Load from actual dataset later
        std::vector<Item> allMovies = {
            Item(1, "The Matrix", {"Action", "Sci-Fi"}, 8.7),
            Item(2, "The Godfather", {"Crime", "Drama"}, 9.2),
            Item(3, "Inception", {"Action", "Sci-Fi", "Thriller"}, 8.8),
            Item(4, "The Shawshank Redemption", {"Drama"}, 9.3),
            Item(5, "Pulp Fiction", {"Crime", "Drama"}, 8.9),
            Item(6, "The Dark Knight", {"Action", "Crime", "Drama"}, 9.0),
            Item(7, "Forrest Gump", {"Drama", "Romance"}, 8.8),
            Item(8, "Fight Club", {"Drama"}, 8.8),
            Item(9, "Star Wars", {"Action", "Adventure", "Fantasy"}, 8.6),
            Item(10, "Interstellar", {"Adventure", "Drama", "Sci-Fi"}, 8.6)
        };
        
        std::cout << "Loaded " << allMovies.size() << " movies" << std::endl;
        
        // 4. Get recommendations using rule-based filtering
        std::cout << "Applying rule-based filtering..." << std::endl;
        std::vector<Item> recommendations = recommender->recommend(preferredGenres, allMovies);
        
        std::cout << "Generated " << recommendations.size() << " recommendations" << std::endl;
        
        // 5. TODO: Call Python ML for further refinement (future step)
        // callPythonML();
        
        // 6. Write output.json
        json outputData;
        outputData["recommendations"] = json::array();
        
        for (const auto& item : recommendations) {
            json movieJson;
            movieJson["id"] = item.getId();
            movieJson["title"] = item.getTitle();
            movieJson["genres"] = item.getGenres();
            movieJson["rating"] = item.getRating();
            outputData["recommendations"].push_back(movieJson);
        }
        
        std::ofstream outputFile(outputPath);
        if (!outputFile.is_open()) {
            throw std::runtime_error("Could not open output file: " + outputPath);
        }
        
        outputFile << outputData.dump(4);  // Pretty print with 4 spaces
        outputFile.close();
        
        std::cout << "Results written to: " << outputPath << std::endl;
        
    } catch (const std::exception& e) {
        handleError(e);
        throw;
    }
}

void Application::loadItems(const std::string& path) {
    // TODO: Load items from dataset file
    std::cout << "Loading items from: " << path << std::endl;
}

void Application::writeIntermediateData(const std::string& path) {
    // TODO: Write intermediate data for Python ML
    std::cout << "Writing intermediate data to: " << path << std::endl;
}

void Application::callPythonML() {
    // TODO: Call Python ML script
    std::cout << "Calling Python ML module..." << std::endl;
    // system("python ../python/recommender/model.py");
}

void Application::handleError(const std::exception& e) {
    std::cerr << "Application Error: " << e.what() << std::endl;
}