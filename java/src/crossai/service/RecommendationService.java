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
    List<Item> getRecommendations(User user);
}
