package crossai.model;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the User class.
 * Tests creation, genre management, and error handling.
 */

public class UserTest {
    private User user;

    @BeforeEach
    public void setUp() {
        user = new User("John Doe", 25);
    }

    /**
     * Test 1: Constructor should create user with valid data
     */
    @Test
    public void testConstructorWithValidData() {
        assertEquals("John Doe", user.getName());
        assertEquals(25, user.getAge());
        assertTrue(user.getPreferredGenres().isEmpty());
    }

    /**
     * Test 2: Constructor should reject null name
     */
    @Test
    public void testConstructorWithNullName() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new User(null, 25);
        });
        
        assertEquals("Name cannot be null or empty!", exception.getMessage());
    }

    /**
     * Test 3: Constructor should reject empty name
     */
    @Test
    public void testConstructorWithEmptyName() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new User("", 25);
        });
        
        assertEquals("Name cannot be null or empty!", exception.getMessage());
    }

    /**
     * Test 4: Constructor should reject negative age
     */
    @Test
    public void testConstructorWithNegativeAge() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new User("John", -5);
        });
        
        assertEquals("Age can't be negative ;)", exception.getMessage());
    }

    /**
     * Test 5: Adding a single genre should work
     */
    @Test
    public void testAddSingleGenre() {
        user.addGenre("Action");
        
        List<String> genres = user.getPreferredGenres();
        assertEquals(1, genres.size());
        assertTrue(genres.contains("Action"));
    }

    /**
     * Test 6: Adding duplicate genre should not create duplicates
     */
    @Test
    public void testAddDuplicateGenre() {
        user.addGenre("Action");
        user.addGenre("Action");
        user.addGenre("Action");
        
        List<String> genres = user.getPreferredGenres();
        assertEquals(1, genres.size()); // Should still be 1
    }

    /**
     * Test 7: Adding null genre should throw exception
     */
    @Test
    public void testAddNullGenre() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            user.addGenre(null);
        });
        
        assertEquals("Genre cannot be null or empty!", exception.getMessage());
    }

    /**
     * Test 8: Adding multiple genres at once
     */
    @Test
    public void testAddMultipleGenres() {
        List<String> genres = Arrays.asList("Action", "Comedy", "Sci-Fi");
        user.addPreferredGenres(genres);
        
        List<String> userGenres = user.getPreferredGenres();
        assertEquals(3, userGenres.size());
        assertTrue(userGenres.contains("Action"));
        assertTrue(userGenres.contains("Comedy"));
        assertTrue(userGenres.contains("Sci-Fi"));
    }

    /**
     * Test 9: toString should return formatted string
     */
    @Test
    public void testToString() {
        user.addGenre("Drama");
        String result = user.toString();
        
        assertTrue(result.contains("John Doe"));
        assertTrue(result.contains("25"));
        assertTrue(result.contains("Drama"));
    }

    /**
     * Test 10: Returned genre list should be a copy (encapsulation test)
     */
    @Test
    public void testGenreListEncapsulation() {
        user.addGenre("Horror");
        List<String> genres = user.getPreferredGenres();
        
        // Try to modify the returned list
        genres.add("Romance");
        
        // Assert - original user's list should NOT be modified
        assertEquals(1, user.getPreferredGenres().size());
        assertFalse(user.getPreferredGenres().contains("Romance"));
    }
}
