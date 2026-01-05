#include "Genre.h"
#include <algorithm>
#include <cctype>

// Helper function to convert string to lowercase
static std::string toLower(std::string str) {
    std::transform(str.begin(), str.end(), str.begin(),
        [](unsigned char c) { return std::tolower(c); });
    return str;
}

Genre stringToGenre(const std::string& str) {
    std::string lower = toLower(str);
    
    // Remove spaces for matching (e.g., "Science Fiction" -> "sciencefiction")
    lower.erase(std::remove(lower.begin(), lower.end(), ' '), lower.end());
    
    if (lower == "action") return Genre::ACTION;
    if (lower == "adventure") return Genre::ADVENTURE;
    if (lower == "animation") return Genre::ANIMATION;
    if (lower == "comedy") return Genre::COMEDY;
    if (lower == "crime") return Genre::CRIME;
    if (lower == "documentary") return Genre::DOCUMENTARY;
    if (lower == "drama") return Genre::DRAMA;
    if (lower == "family") return Genre::FAMILY;
    if (lower == "fantasy") return Genre::FANTASY;
    if (lower == "history") return Genre::HISTORY;
    if (lower == "horror") return Genre::HORROR;
    if (lower == "music") return Genre::MUSIC;
    if (lower == "mystery") return Genre::MYSTERY;
    if (lower == "romance") return Genre::ROMANCE;
    
    // Science Fiction variations
    if (lower == "sciencefiction" || lower == "science-fiction" || 
        lower == "sci-fi" || lower == "scifi") {
        return Genre::SCIENCE_FICTION;
    }
    
    if (lower == "tvmovie" || lower == "tv-movie" || lower == "tv") {
        return Genre::TV_MOVIE;
    }
    
    if (lower == "thriller") return Genre::THRILLER;
    if (lower == "war") return Genre::WAR;
    if (lower == "western") return Genre::WESTERN;
    
    throw std::invalid_argument("Unknown genre: " + str);
}

std::string genreToString(Genre genre) {
    switch (genre) {
        case Genre::ACTION: return "Action";
        case Genre::ADVENTURE: return "Adventure";
        case Genre::ANIMATION: return "Animation";
        case Genre::COMEDY: return "Comedy";
        case Genre::CRIME: return "Crime";
        case Genre::DOCUMENTARY: return "Documentary";
        case Genre::DRAMA: return "Drama";
        case Genre::FAMILY: return "Family";
        case Genre::FANTASY: return "Fantasy";
        case Genre::HISTORY: return "History";
        case Genre::HORROR: return "Horror";
        case Genre::MUSIC: return "Music";
        case Genre::MYSTERY: return "Mystery";
        case Genre::ROMANCE: return "Romance";
        case Genre::SCIENCE_FICTION: return "Science Fiction";
        case Genre::TV_MOVIE: return "TV Movie";
        case Genre::THRILLER: return "Thriller";
        case Genre::WAR: return "War";
        case Genre::WESTERN: return "Western";
        default: return "Unknown";
    }
}

std::vector<Genre> getAllGenres() {
    return {
        Genre::ACTION,
        Genre::ADVENTURE,
        Genre::ANIMATION,
        Genre::COMEDY,
        Genre::CRIME,
        Genre::DOCUMENTARY,
        Genre::DRAMA,
        Genre::FAMILY,
        Genre::FANTASY,
        Genre::HISTORY,
        Genre::HORROR,
        Genre::MUSIC,
        Genre::MYSTERY,
        Genre::ROMANCE,
        Genre::SCIENCE_FICTION,
        Genre::TV_MOVIE,
        Genre::THRILLER,
        Genre::WAR,
        Genre::WESTERN
    };
}

bool isValidGenre(const std::string& str) {
    try {
        stringToGenre(str);
        return true;
    } catch (const std::invalid_argument&) {
        return false;
    }
}