#include "Item.h"
#include <algorithm>

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