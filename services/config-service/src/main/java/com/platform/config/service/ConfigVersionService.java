package com.platform.config.service;

import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

@Service
public class ConfigVersionService {

    private final ConcurrentHashMap<String, Integer> versions = new ConcurrentHashMap<>();

    public int nextVersion(String key) {
        return versions.merge(key, 1, Integer::sum);
    }

    public int getVersion(String key) {
        return versions.getOrDefault(key, 1);
    }
}