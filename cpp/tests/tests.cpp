#include <gtest/gtest.h>
#include <vector>
#include <string>

// Include your headers
#include "Item.h"
#include "RuleBasedRecommender.h"
#include "Genre.h"

// ==========================================
// 1. Item Tests (Testing Data Class Logic)
// ==========================================

class ItemTest : public ::testing::Test {
protected:
    Item* actionMovie;
    Item* dramaMovie;

    // JUnit @BeforeEach equivalent
    void SetUp() override {
        std::vector<std::string> actionGenres = {"Action", "Adventure"};
        std::vector<std::string> dramaGenres = {"Drama"};
        
        // Item(id, title, genres, rating)
        actionMovie = new Item(1, "Mad Max", actionGenres, 8.5);
        dramaMovie = new Item(2, "The Godfather", dramaGenres, 9.2);
    }

    // JUnit @AfterEach equivalent
    void TearDown() override {
        delete actionMovie;
        delete dramaMovie;
    }
};

TEST_F(ItemTest, GettersReturnCorrectValues) {
    EXPECT_EQ(actionMovie->getTitle(), "Mad Max");
    EXPECT_EQ(actionMovie->getId(), 1);
    EXPECT_DOUBLE_EQ(actionMovie->getRating(), 8.5);
}

TEST_F(ItemTest, MatchesGenresReturnsTrueForMatch) {
    std::vector<std::string> preferences = {"Comedy", "Action"}; // "Action" matches
    EXPECT_TRUE(actionMovie->matchesGenres(preferences));
}

TEST_F(ItemTest, MatchesGenresReturnsFalseForNoMatch) {
    std::vector<std::string> preferences = {"Romance", "Comedy"};
    EXPECT_FALSE(actionMovie->matchesGenres(preferences));
}

TEST_F(ItemTest, ComparisonOperatorSortsByRatingDescending) {
    // Logic check: operator< implementation sorts higher ratings first
    // 9.2 should come BEFORE 8.5
    EXPECT_TRUE(*dramaMovie < *actionMovie); 
}

// ==========================================
// 2. Genre Tests (Testing Static Helpers)
// ==========================================

TEST(GenreTest, StringToGenreHandlesCaseInsensitivity) {
    EXPECT_EQ(stringToGenre("action"), Genre::ACTION);
    EXPECT_EQ(stringToGenre("ACTION"), Genre::ACTION);
    EXPECT_EQ(stringToGenre("Science Fiction"), Genre::SCIENCE_FICTION);
    EXPECT_EQ(stringToGenre("Sci-Fi"), Genre::SCIENCE_FICTION);
}

TEST(GenreTest, StringToGenreThrowsOnInvalidInput) {
    // JUnit assertThrows equivalent
    EXPECT_THROW(stringToGenre("NotAGenre"), std::invalid_argument);
}

// ==========================================
// 3. Recommender Tests (Testing The Algorithm)
// ==========================================

class RecommenderTest : public ::testing::Test {
protected:
    RuleBasedRecommender recommender;
    std::vector<Item> inventory;

    void SetUp() override {
        // Create a small fake database of items
        inventory.push_back(Item(1, "Action Hit", {"Action"}, 8.0));
        inventory.push_back(Item(2, "Drama Hit", {"Drama"}, 9.0));
        inventory.push_back(Item(3, "Mixed Bag", {"Action", "Comedy"}, 5.0));
        inventory.push_back(Item(4, "Bad Movie", {"Horror"}, 2.0));
    }
};

TEST_F(RecommenderTest, RecommendFiltersAndSortsCorrectly) {
    // User likes "Action"
    std::vector<std::string> preferences = {"Action"};

    std::vector<Item> results = recommender.recommend(preferences, inventory);

    // Assertions
    ASSERT_EQ(results.size(), 2); // Should match "Action Hit" and "Mixed Bag"
    
    // Check Sorting: 
    // "Action Hit" (Score: 1 match * 2.0 + 8.0 rating = 10.0)
    // "Mixed Bag"  (Score: 1 match * 2.0 + 5.0 rating = 7.0)
    // "Action Hit" should be first
    EXPECT_EQ(results[0].getTitle(), "Action Hit");
    EXPECT_EQ(results[1].getTitle(), "Mixed Bag");
}

TEST_F(RecommenderTest, RecommendReturnsEmptyIfNoMatches) {
    std::vector<std::string> preferences = {"Romance"};
    
    std::vector<Item> results = recommender.recommend(preferences, inventory);
    
    EXPECT_TRUE(results.empty());
}

// ==========================================
// 4. Main Runner
// ==========================================

int main(int argc, char **argv) {
    ::testing::InitGoogleTest(&argc, argv);
    return RUN_ALL_TESTS();
}