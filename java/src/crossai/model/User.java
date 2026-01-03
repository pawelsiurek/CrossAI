package crossai.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a user in the movie recommendation system.
 * Stores user information and their preferred movie genres.
 */
public class User {
    private String name;
    private int age;
    private List<Genre> preferredGenres;  // Changed to Genre enum!

    /**
     * Constructor to create a new user.
     * 
     * @param name User's name
     * @param age User's age
     */
    public User(String name, int age) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty!");
        }
        if (age < 0) {
            throw new IllegalArgumentException("Age can't be negative ;)");
        }

        this.name = name;
        this.age = age;
        this.preferredGenres = new ArrayList<>();
    }

    // Getters
    public String getName() { 
        return name; 
    }
    
    public int getAge() { 
        return age; 
    }
    
    public List<Genre> getPreferredGenres() { 
        return new ArrayList<>(preferredGenres); 
    }

    // Managing genres
    public void addGenre(Genre genre) {  
        if (genre == null) {
            throw new IllegalArgumentException("Genre cannot be null!");
        }
        if (!preferredGenres.contains(genre)) {
            preferredGenres.add(genre);
        }
    }
    
    public void addPreferredGenres(List<Genre> genres) {  
        if (genres != null) {
            for (Genre genre : genres) {
                addGenre(genre);
            }
        }
    }

    @Override 
    public String toString() {
        return "User{" +
            "name='" + name + '\'' +
            ", age=" + age +
            ", preferredGenres=" + preferredGenres +
            '}';
    }
}