package crossai.controller;

import java.util.List;

import crossai.model.Genre;
import crossai.model.Item;
import crossai.model.User;
import crossai.service.HybridRecommendationService;
import crossai.service.RecommendationService;

/**
 * Controller class that coordinates between the UI and recommendation service.
 * Follows the MVC pattern - this is the Controller layer.
 * 
 * Responsibilities:
 * - Manage current user
 * - Coordinate recommendation requests
 * - Provide simplified API for UI layer
 */
public class AppController {
    private User currentUser;
    private RecommendationService service;

    public AppController() {
        this.service = new HybridRecommendationService("../shared");
        this.currentUser = null;
    }

    public User createUser(String name, int age) {
        this.currentUser = new User(name, age);
        System.out.println("[CONTROLLER] Created user: " + name);
        return this.currentUser;
    }

    public void setCurrentUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        this.currentUser = user;
        System.out.println("[CONTROLLER] Set current user: " + user.getName());
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void addGenreToCurrentUser(Genre genre) {
        if (currentUser == null) {
            throw new IllegalStateException("No user set. Call createUser() or setCurrentUser() first.");
        }
        currentUser.addGenre(genre);
        System.out.println("[CONTROLLER] Added genre " + genre.getDisplayName() + " to " + currentUser.getName());
    }

    public void addGenresToCurrentUser(List<Genre> genres) {
        if (currentUser == null) {
            throw new IllegalStateException("No user set. Call createUser() or setCurrentUser() first.");
        }
        currentUser.addPreferredGenres(genres);
        System.out.println("[CONTROLLER] Added " + genres.size() + " genres to " + currentUser.getName());
    }

    public List<Item> getRecommendationsForCurrentUser() {
        if (currentUser == null) {
            throw new IllegalStateException("No user set. Call createUser() or setCurrentUser() first.");
        }
        
        if (currentUser.getPreferredGenres().isEmpty()) {
            System.err.println("[WARNING] User has no preferred genres. Recommendations may not be personalized.");
        }
        
        System.out.println("[CONTROLLER] Getting recommendations for: " + currentUser.getName());
        List<Item> recommendations = service.getRecommendations(currentUser);
        System.out.println("[CONTROLLER] Retrieved " + recommendations.size() + " recommendations");
        
        return recommendations;
    }

    public List<Item> getRecommendations(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        return service.getRecommendations(user);
    }

    public void clearCurrentUser() {
        this.currentUser = null;
        System.out.println("[CONTROLLER] Cleared current user");
    }

    public boolean hasCurrentUser() {
        return currentUser != null;
    }
    
    public void close() {
        if (service instanceof HybridRecommendationService) {
            try {
                ((HybridRecommendationService) service).close();
                System.out.println("[CONTROLLER] Closed hybrid service");
            } catch (Exception e) {
                System.err.println("[ERROR] Failed to close hybrid service: " + e.getMessage());
            }
        }
    }
}