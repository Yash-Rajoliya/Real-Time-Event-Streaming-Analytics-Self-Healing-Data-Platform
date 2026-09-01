// services/gateway-service/src/main/java/com/platform/gateway/config/RateLimiterConfig.java
package com.platform.gateway.config;

import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class RateLimiterConfig {

    /**
     * Configures Token Bucket algorithm parameters for high-throughput traffic control via Redis.
     */
    @Bean
    @Primary
    public RedisRateLimiter redisRateLimiter() {
        // replenishRate = 2000 tokens/sec, burstCapacity = 4000 tokens, requestedTokens = 1
        return new RedisRateLimiter(2000, 4000, 1);
    }
}