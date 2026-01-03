package crossai.service;

import java.util.List;

import crossai.model.Item;
import crossai.model.User;

/**
 * Interface for recommendation services.
 * Defines the contract that all recommendation implementations must follow.
 * 
 * This allows for multiple recommendation strategies (hybrid, collaborative, content-based)
 * while maintaining a consistent API.
 */

public interface RecommendationService {
    /**
     * Get personalized recommendations for a user.
     * 
     * @param user The user to get recommendations for
     * @return List of recommended items
     * @throws IllegalArgumentException if user is null
     */
    
    List<Item> getRecommendations(User user);
}
