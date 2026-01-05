#include "RuleBasedRecommender.h"
#include <algorithm>

std::vector<Item> RuleBasedRecommender::recommend(
    const std::vector<std::string>& preferredGenres,
    const std::vector<Item>& items) {

    std::vector<Item> recommendations;
    std::vector<std::pair<Item, double>> scoredItems;

    // Score each item
    for (const auto& item : items) {
        double score = calculateScore(item, preferredGenres);
        if (score > 0) {  // Only include items with positive score
            scoredItems.push_back({item, score});
        }
    }

    // Sort by score (highest first)
    std::sort(scoredItems.begin(), scoredItems.end(),
        [](const auto& a, const auto& b) {
            return a.second > b.second;
        });

    // Take top 10 recommendations
    int count = std::min(10, static_cast<int>(scoredItems.size()));
    for (int i = 0; i < count; i++) {
        recommendations.push_back(scoredItems[i].first);
    }

    return recommendations;
}

double RuleBasedRecommender::calculateScore(
    const Item& item,
    const std::vector<std::string>& preferredGenres) {

    double score = 0.0;

    // Count how many preferred genres match
    int matchCount = 0;
    for (const auto& itemGenre : item.getGenres()) {
        if (std::find(preferredGenres.begin(), preferredGenres.end(), itemGenre)
            != preferredGenres.end()) {
            matchCount++;
            }
    }

    // Score based on genre matches and rating
    if (matchCount > 0) {
        score = matchCount * 2.0 + item.getRating();
    }

    return score;
}