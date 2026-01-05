package crossai.service;

import java.util.ArrayList;
import java.util.List;

import crossai.model.Item;
import crossai.model.User;

/**
 * Mock implementation of recommendation service for testing.
 * Returns dummy data without requiring file I/O or external dependencies.
 * 
 * Useful for:
 * - Unit testing other components
 * - Development when C++ engine is not ready
 * - Quick demos and prototyping
 */
public class MockRecommendationService extends BaseRecommendationService {
    
    private List<Item> mockDatabase;

    public MockRecommendationService(String dataDirectory) {
        super(dataDirectory);
        initializeMockDatabase();
    }
    
    /**
     * Initialize mock movie database with sample data.
     */
    private void initializeMockDatabase() {
        mockDatabase = new ArrayList<>();
        
        // Action movies
        mockDatabase.add(new Item(1, "The Dark Knight", "Batman fights Joker in Gotham"));
        mockDatabase.add(new Item(2, "Mad Max: Fury Road", "Post-apocalyptic chase"));
        mockDatabase.add(new Item(3, "John Wick", "Assassin seeks revenge"));
        
        // Sci-Fi movies
        mockDatabase.add(new Item(4, "Inception", "Dreams within dreams"));
        mockDatabase.add(new Item(5, "The Matrix", "Reality is simulation"));
        mockDatabase.add(new Item(6, "Interstellar", "Space exploration"));
        
        // Comedy movies
        mockDatabase.add(new Item(7, "The Grand Budapest Hotel", "Quirky hotel adventure"));
        mockDatabase.add(new Item(8, "Superbad", "Teenage party comedy"));
        
        // Drama movies
        mockDatabase.add(new Item(9, "The Shawshank Redemption", "Prison drama"));
        mockDatabase.add(new Item(10, "Forrest Gump", "Life story of simple man"));
        
        // Horror movies
        mockDatabase.add(new Item(11, "The Shining", "Haunted hotel horror"));
        mockDatabase.add(new Item(12, "Get Out", "Psychological horror"));
    }
    
    @Override
    public List<Item> getRecommendations(User user) {
        validateUser(user);
        
        // Check cache first
        String cacheKey = "mock_" + user.getName();
        var cachedResult = cache.get(cacheKey);
        
        if (cachedResult.isPresent()) {
            logRecommendation(user, cachedResult.get().size());
            if (loggingEnabled) {
                System.out.println("[CACHE] Returning cached mock recommendations");
            }
            return cachedResult.get();
        }
        
        // For mock: just return first 5 items
        // In real implementation, would filter by user's preferred genres
        List<Item> recommendations = new ArrayList<>();
        int count = Math.min(5, mockDatabase.size());
        
        for (int i = 0; i < count; i++) {
            recommendations.add(mockDatabase.get(i));
        }
        
        // Cache the results
        cache.put(cacheKey, recommendations);
        
        // Log using base class method
        logRecommendation(user, recommendations.size());
        
        if (loggingEnabled) {
            System.out.println("[MOCK] Returned mock data (no file I/O)");
        }
        
        return recommendations;
    }
    
    public List<Item> getAllMockItems() { return new ArrayList<>(mockDatabase); }
    
    public void addMockItem(Item item) {
        if (item != null) {
            mockDatabase.add(item);
        }
    }
}