package crossai.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Generic cache implementation for storing and retrieving objects.
 * Uses a HashMap internally for fast lookups. (basically a HashMap wrapper :D)
 * 
 * @param <T> The type of objects to cache
 */

public class Cache<T> {
    private final Map<String, T> cache;
    private final int maxSize;

    // unlimited size cache
    public Cache() {
        this(Integer.MAX_VALUE);
    }

    // limited size cache :)
    public Cache(int maxSize) {
        this.cache = new HashMap<>();
        this.maxSize = maxSize;
    }

    public void put(String key, T value) {
        if (value == null) {
            throw new IllegalArgumentException("Cannot cache null value");
        }
        if (cache.size() >= maxSize) {
            cache.clear();
        }

        cache.put(key, value);
    }

    public Optional<T> get(String key) {
        return Optional.ofNullable(cache.get(key));
    }

    public boolean contains(String key) {
        return cache.containsKey(key);
    }

    public void remove(String key) {
        cache.remove(key);
    }

    public void clear() {
        cache.clear();
    }

    public int size() {
        return cache.size();
    }

}
