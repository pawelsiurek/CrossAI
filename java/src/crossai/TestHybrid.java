package crossai;

import java.util.List;

import crossai.model.Genre;
import crossai.model.Item;
import crossai.model.User;
import crossai.service.HybridRecommendationService;

/**
 * Quick manual test for HybridRecommendationService.
 * This is just for testing - will delete after.
 */
public class TestHybrid {
    public static void main(String[] args) {
        System.out.println("=== Testing HybridRecommendationService ===\n");
        
        // Create a test user
        User user = new User("Alice", 28);
        user.addGenre(Genre.ACTION);
        user.addGenre(Genre.SCIENCE_FICTION);
        user.addGenre(Genre.THRILLER);
        
        System.out.println("User: " + user.getName());
        System.out.println("Age: " + user.getAge());
        System.out.println("Genres: " + user.getPreferredGenres());
        System.out.println();
        
        // Create service
        HybridRecommendationService service = 
            new HybridRecommendationService("../shared");
        
        try {
            // Get recommendations
            System.out.println("Getting recommendations...\n");
            List<Item> recommendations = service.getRecommendations(user);
            
            System.out.println("\n=== Recommendations ===");
            for (Item item : recommendations) {
                System.out.println("- " + item.getTitle());
                System.out.println("  " + item.getDescription());
                System.out.println();
            }
            
            System.out.println("Total: " + recommendations.size() + " movies");
            
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                service.close();
                System.out.println("\n[CLEANUP] Resources closed");
            } catch (Exception e) {
                System.err.println("Failed to close: " + e.getMessage());
            }
        }
    }
}