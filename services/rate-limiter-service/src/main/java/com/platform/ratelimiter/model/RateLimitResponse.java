package com.platform.ratelimiter.model;

import lombok.Data;

@Data
public class RateLimitRequest {
    private String clientId;
    private int capacity;
    private int refillRate;
}