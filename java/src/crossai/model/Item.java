package crossai.model;

import java.util.Objects;

/**
 * Represents an item (movie) in the recommendation system.
 * This class holds basic information about a recommendable item.
 */

public class Item {
    private int id;
    private String title;
    private String description;

    public Item(int id, String title, String description) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be null or empty");
        }

        this.id = id;
        this.title = title;
        this.description = description != null ? description : ""; // if null we set it to empty string
    }

    public Item(int id, String title) {
        this(id, title, "");
    }

    // getters
    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) { // overriding equals so we can compare objects by ID
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        Item item = (Item) o;
        return id == item.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id); // needs to be consistent with the above override
    }

}
