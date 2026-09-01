// services/gateway-service/src/main/java/com/platform/gateway/GatewayApplication.java
package com.platform.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import reactor.core.publisher.Mono;

import java.util.Objects;

@SpringBootApplication
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

    /**
     * Resolves client identity for Redis-backed request rate limiting.
     * Uses Authorization header/IP address to enforce per-client thresholds.
     */
    @Bean
    public KeyResolver userKeyResolver() {
        return exchange -> {
            String apiKey = exchange.getRequest().getHeaders().getFirst("X-API-Key");
            if (apiKey != null && !apiKey.isBlank()) {
                return Mono.just(apiKey);
            }
            
            String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                return Mono.just(authHeader.substring(7));
            }

            return Mono.just(Objects.requireNonNull(
                exchange.getRequest().getRemoteAddress()).getAddress().getHostAddress());
        };
    }
}