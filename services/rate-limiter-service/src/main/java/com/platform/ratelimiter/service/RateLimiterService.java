package com.platform.ratelimiter.service;

import com.platform.ratelimiter.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RateLimiterService {

    private final TokenBucketService bucketService;

    public RateLimitResponse check(RateLimitRequest request) {

        boolean allowed = bucketService.allowRequest(
                request.getClientId(),
                request.getCapacity(),
                request.getRefillRate()
        );

        long remaining = bucketService.getRemaining(request.getClientId());

        return RateLimitResponse.builder()
                .allowed(allowed)
                .remainingTokens(remaining)
                .build();
    }
}