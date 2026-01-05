#pragma once
#include <string>
#include <vector>
#include <stdexcept>

/**
 * Genre enumeration matching the 19 TMDB genres.
 * Matches the Java Genre enum for consistency across the system.
 */
enum class Genre {
    ACTION,
    ADVENTURE,
    ANIMATION,
    COMEDY,
    CRIME,
    DOCUMENTARY,
    DRAMA,
    FAMILY,
    FANTASY,
    HISTORY,
    HORROR,
    MUSIC,
    MYSTERY,
    ROMANCE,
    SCIENCE_FICTION,
    TV_MOVIE,
    THRILLER,
    WAR,
    WESTERN
};


Genre stringToGenre(const std::string& str);

std::string genreToString(Genre genre);

std::vector<Genre> getAllGenres();

bool isValidGenre(const std::string& str);