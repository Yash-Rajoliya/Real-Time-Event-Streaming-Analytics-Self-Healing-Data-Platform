package com.platform.selfhealing.recovery;

import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class CircuitBreakerHandler {

    private final ConcurrentHashMap<String, Boolean> circuitMap = new ConcurrentHashMap<>();

    public void open(String service) {
        circuitMap.put(service, true);
    }

    public void close(String service) {
        circuitMap.put(service, false);
    }

    public boolean isOpen(String service) {
        return circuitMap.getOrDefault(service, false);
    }
}