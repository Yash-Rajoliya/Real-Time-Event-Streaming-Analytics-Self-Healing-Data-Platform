package com.platform.featureflag.cache;

import com.platform.featureflag.model.FeatureFlag;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class FeatureCache {

    private final ConcurrentHashMap<String, FeatureFlag> cache = new ConcurrentHashMap<>();

    public void put(FeatureFlag flag) {
        cache.put(flag.getKey(), flag);
    }

    public FeatureFlag get(String key) {
        return cache.get(key);
    }
}