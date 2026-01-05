#include "Item.h"
#include <algorithm>
#include <iostream>
#include <sstream>

Item::Item(int id, const std::string& title, const std::vector<std::string>& genres, double rating)
    : id(id), title(title), genres(genres), rating(rating) {
}

int Item::getId() const {
    return id;
}

std::string Item::getTitle() const {
    return title;
}

std::vector<std::string> Item::getGenres() const {
    return genres;
}

double Item::getRating() const {
    return rating;
}

bool Item::matchesGenres(const std::vector<std::string>& preferredGenres) const {
    // Check if any of the item's genres match the user's preferred genres
    for (const auto& genre : genres) {
        if (std::find(preferredGenres.begin(), preferredGenres.end(), genre) != preferredGenres.end()) {
            return true;
        }
    }
    return false;
}

bool Item::operator==(const Item& other) const {
    return this->id == other.id;
}

bool Item::operator!=(const Item& other) const {
    return !(*this == other);  // Use the == operator
}

bool Item::operator<(const Item& other) const {
    if (this->rating != other.rating) {
        return this->rating > other.rating;  // Higher rating comes first
    }
    return this->id < other.id;
}

bool Item::operator>(const Item& other) const {
    return other < *this;  // Use the < operator
}

std::ostream& operator<<(std::ostream& os, const Item& item) {
    os << "Item{id=" << item.id 
       << ", title=\"" << item.title << "\""
       << ", rating=" << item.rating
       << ", genres=[";
    
    for (size_t i = 0; i < item.genres.size(); ++i) {
        os << item.genres[i];
        if (i < item.genres.size() - 1) {
            os << ", ";
        }
    }
    
    os << "]}";
    return os;
}