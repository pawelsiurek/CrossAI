package crossai.service;


import java.util.ArrayList;
import java.util.List;

import crossai.model.Item;
import crossai.model.User;
import crossai.util.Cache;


public abstract class BaseRecommendationService implements RecommendationService {
    protected String dataDirectory;
    protected Cache<List<Item>> cache;
    protected boolean loggingEnabled;

    protected BaseRecommendationService(String dataDirectory) {
        if (dataDirectory == null || dataDirectory.trim().isEmpty()) {
            throw new IllegalArgumentException("Data directory cannot be null or empty");
        }
        this.dataDirectory = dataDirectory;
        this.cache = new Cache<>();
        this.loggingEnabled = true;
    }

    @Override // to be implemented by subclasses
    public abstract List<Item> getRecommendations(User user);

    // logging a recommendation event
    protected void logRecommendation(User user, int count) {
        if (loggingEnabled) {
            System.out.println("[RECOMMENDATION] Generated " + count +
                                " recommendations for user: " + user.getName());
        }
    }

    // handling errors that occur during recommendation generation
    protected List<Item> handleError(Exception e, User user) {
        System.err.println("[ERROR] Failed to generate recommendations for " + 
                            user.getName() + ": " + e.getMessage());
        e.printStackTrace();
        return new ArrayList<>();
    }

    // validate that user object is valid for generating recommendations
    protected void validateUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        if (user.getName() == null || user.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("User must have a valid name");
        }
    }

    // logging enable/disable
    public void setLoggingEnabled(boolean enabled) {
        this.loggingEnabled = enabled;
    }

    public String getDataDirectory() { return dataDirectory; }

    // cache clearing
    public void clearCache() {
        cache.clear();
        if (loggingEnabled) {
            System.out.println("[CACHE] Cache cleared");
        }
    }
}
