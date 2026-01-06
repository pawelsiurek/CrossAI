package crossai;

import crossai.model.Genre;
import crossai.model.User;
import crossai.model.Item;
import crossai.service.HybridRecommendationService;
import java.util.List;

public class FullSystemTest {
    public static void main(String[] args) {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║  CrossAI FULL SYSTEM TEST              ║");
        System.out.println("╚════════════════════════════════════════╝\n");
        
        User user = new User("Mateusz", 22);
        user.addGenre(Genre.ACTION);
        user.addGenre(Genre.SCIENCE_FICTION);
        user.addGenre(Genre.DRAMA);
        
        System.out.println("👤 User: " + user.getName());
        System.out.println("🎭 Genres: " + user.getPreferredGenres());
        System.out.println("\n⏳ Getting ML recommendations...\n");
        
        HybridRecommendationService service = 
            new HybridRecommendationService("../shared");
        
        List<Item> recommendations = service.getRecommendations(user);
        
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║       YOUR RECOMMENDATIONS             ║");
        System.out.println("╚════════════════════════════════════════╝\n");
        
        int count = 1;
        for (Item item : recommendations) {
            System.out.println("─────────────────────────────────────");
            System.out.println(count++ + ". " + item.getTitle());
            System.out.println("🎬 ID: " + item.getId());
            System.out.println("🎭 Genres: " + item.getGenresAsString());
            System.out.printf("⭐ Rating: %.1f/10\n\n", item.getRating());
        }
        
        System.out.println("✅ Total: " + recommendations.size() + " movies!\n");
    }
}
