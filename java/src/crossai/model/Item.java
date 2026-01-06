package crossai.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Represents an item (movie) in the recommendation system.
 * This class holds information about a recommendable item including
 * genres and rating from the ML/C++ recommendation engine.
 */
public class Item {
    private int id;
    private String title;
    private String description;
    private List<String> genres;
    private double rating;

    // Full constructor
    public Item(int id, String title, String description, List<String> genres, double rating) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be null or empty");
        }

        this.id = id;
        this.title = title;
        this.description = description != null ? description : "";
        this.genres = genres != null ? new ArrayList<>(genres) : new ArrayList<>();
        this.rating = rating;
    }

    // Constructor without genres and rating (for backward compatibility)
    public Item(int id, String title, String description) {
        this(id, title, description, new ArrayList<>(), 0.0);
    }

    // Constructor with only id and title (for backward compatibility)
    public Item(int id, String title) {
        this(id, title, "", new ArrayList<>(), 0.0);
    }

    // Getters
    public int getId() { 
        return id; 
    }
    
    public String getTitle() { 
        return title; 
    }
    
    public String getDescription() { 
        return description; 
    }
    
    public List<String> getGenres() { 
        return Collections.unmodifiableList(genres); 
    }
    
    public double getRating() { 
        return rating; 
    }
    
    // Helper method to get genres as a formatted string
    public String getGenresAsString() {
        if (genres.isEmpty()) {
            return "Unknown";
        }
        return String.join(", ", genres);
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", genres=" + genres +
                ", rating=" + rating +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        Item item = (Item) o;
        return id == item.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}