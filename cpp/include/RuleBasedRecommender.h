#pragma once
#include "Recommender.h"

class RuleBasedRecommender : public Recommender {
public:
    std::vector<Item> recommend(
        const std::vector<std::string>& preferredGenres,
        const std::vector<Item>& items
    ) override;
    
private:
    double calculateScore(const Item& item, const std::vector<std::string>& preferredGenres);
};