package crossai.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a user in the movie recommendation system.
 * Stores user information and their preferred movie genres.
 **/

public class User {
    private String name;
    private int age;
    private List<String> preferredGenres;

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

    // getters
    public String getName() { return name; }
    public int getAge() { return age; }
    public List<String> getPreferredGenres() { return new ArrayList<>(preferredGenres); }

    // managing genres
    public void addGenre(String genre) {
        if (genre == null || genre.trim().isEmpty()) {
            throw new IllegalArgumentException("Genre cannot be null or empty!");
        }
        if (!preferredGenres.contains(genre)) {
            preferredGenres.add(genre);
        }
    }
    public void addPreferredGenres(List<String> genres) {
        if (genres != null) {
            for (String genre : genres) {
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
