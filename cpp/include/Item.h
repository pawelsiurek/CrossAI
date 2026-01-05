#pragma once
#include <string>
#include <vector>

class Item {
private:
    int id;
    std::string title;
    std::vector<std::string> genres;
    double rating;

public:
    Item(int id, const std::string& title, const std::vector<std::string>& genres, double rating);
    
    // Getters
    int getId() const;
    std::string getTitle() const;
    std::vector<std::string> getGenres() const;
    double getRating() const;
    
    // Check if item matches preferred genres
    bool matchesGenres(const std::vector<std::string>& preferredGenres) const;

    bool operator==(const Item& other) const; // item comparison by ID
    bool operator!=(const Item& other) const;
    bool operator<(const Item& other) const; // used for sorting by rating
    bool operator>(const Item& other) const;

    friend std::ostream& operator<<(std::ostream& os, const Item& item);
};