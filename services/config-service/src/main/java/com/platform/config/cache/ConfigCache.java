package com.platform.config.cache;

import com.platform.config.model.AppConfig;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class ConfigCache {

    private final ConcurrentHashMap<String, AppConfig> cache = new ConcurrentHashMap<>();

    public void put(String key, AppConfig config) {
        cache.put(key, config);
    }

    public AppConfig get(String key) {
        return cache.get(key);
    }

    public void evict(String key) {
        cache.remove(key);
    }
}