package com.platform.ratelimiter.service;

import com.platform.ratelimiter.util.KeyBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenBucketService {

    private final RedisTemplate<String, Object> redis;

    public synchronized boolean allowRequest(
            String clientId,
            int capacity,
            int refillRate
    ) {

        String tokenKey = KeyBuilder.tokenKey(clientId);
        String timeKey = KeyBuilder.timestampKey(clientId);

        long now = System.currentTimeMillis();

        Integer tokens = (Integer) redis.opsForValue().get(tokenKey);
        Long lastRefill = (Long) redis.opsForValue().get(timeKey);

        if (tokens == null) {
            tokens = capacity;
            lastRefill = now;
        }

        // refill logic
        long delta = now - lastRefill;
        int refillTokens = (int) (delta / 1000 * refillRate);

        tokens = Math.min(capacity, tokens + refillTokens);
        lastRefill = now;

        if (tokens > 0) {
            tokens--;
            redis.opsForValue().set(tokenKey, tokens);
            redis.opsForValue().set(timeKey, lastRefill);
            return true;
        }

        redis.opsForValue().set(tokenKey, tokens);
        redis.opsForValue().set(timeKey, lastRefill);

        return false;
    }

    public long getRemaining(String clientId) {
        Integer tokens = (Integer) redis.opsForValue().get(
                KeyBuilder.tokenKey(clientId)
        );
        return tokens == null ? 0 : tokens;
    }
}