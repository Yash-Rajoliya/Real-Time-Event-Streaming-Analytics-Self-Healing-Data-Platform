// services/ingestion-service/src/main/java/com/platform/ingestion/config/RateLimiterConfig.java
package com.platform.ingestion.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class RateLimiterConfig {

    private final Map<String, Bucket> bucketCache = new ConcurrentHashMap<>();

    @Bean
    public RateLimiterResolver rateLimiterResolver() {
        return apiKey -> bucketCache.computeIfAbsent(apiKey, k -> {
            Bandwidth limit = Bandwidth.builder()
                    .capacity(1000)
                    .refillGreedy(1000, Duration.ofSeconds(1))
                    .build();
            return Bucket.builder().addLimit(limit).build();
        });
    }

    @FunctionalInterface
    public interface RateLimiterResolver {
        Bucket resolveBucket(String key);
    }
}