package crossai.model;

/**
 * Enumeration of movie genres from TMDB (The Movie Database).
 * These genres match the official TMDB genre list used in the Kaggle dataset.
 * 
 * Dataset: https://www.kaggle.com/datasets/rounakbanik/the-movies-dataset
 */

public enum Genre {
    ACTION("Action"),
    ADVENTURE("Adventure"),
    ANIMATION("Animation"),
    COMEDY("Comedy"),
    CRIME("Crime"),
    DOCUMENTARY("Documentary"),
    DRAMA("Drama"),
    FAMILY("Family"),
    FANTASY("Fantasy"),
    HISTORY("History"),
    HORROR("Horror"),
    MUSIC("Music"),
    MYSTERY("Mystery"),
    ROMANCE("Romance"),
    SCIENCE_FICTION("Science Fiction"),
    TV_MOVIE("TV Movie"),
    THRILLER("Thriller"),
    WAR("War"),
    WESTERN("Western");
    
    private final String displayName;
    
    Genre(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() { return displayName; }
    
    /**
     * Parse a string to a Genre enum. (needed for json later on)
     * Case-insensitive matching against both enum name and display name.
     */

    public static Genre fromString(String genreString) {
        if (genreString == null || genreString.trim().isEmpty()) {
            return null;
        }
        
        String normalized = genreString.trim();
        
        for (Genre genre : Genre.values()) {
            // Match against display name (e.g., "Science Fiction")
            if (genre.displayName.equalsIgnoreCase(normalized)) {
                return genre;
            }
            // Match against enum name (e.g., "SCIENCE_FICTION")
            if (genre.name().equalsIgnoreCase(normalized)) {
                return genre;
            }
            // Handle common variations
            if (genre == SCIENCE_FICTION && 
                (normalized.equalsIgnoreCase("Sci-Fi") || 
                 normalized.equalsIgnoreCase("SciFi"))) {
                return genre;
            }
        }
        return null;
    }
    
    @Override
    public String toString() {
        return displayName;
    }
}