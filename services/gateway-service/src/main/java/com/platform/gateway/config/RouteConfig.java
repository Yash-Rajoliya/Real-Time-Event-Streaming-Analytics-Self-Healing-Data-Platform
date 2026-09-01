// services/gateway-service/src/main/java/com/platform/gateway/config/RouteConfig.java
package com.platform.gateway.config;

import com.platform.gateway.filter.AuthFilter;
import com.platform.gateway.filter.CorrelationIdFilter;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouteConfig {

    @Bean
    public RouteLocator customRouteLocator(
            RouteLocatorBuilder builder,
            AuthFilter authFilter,
            CorrelationIdFilter correlationIdFilter,
            RedisRateLimiter redisRateLimiter) {

        return builder.routes()
                // Ingestion API Route - High Throughput
                .route("ingestion-service-route", r -> r
                        .path("/api/v1/events/**")
                        .filters(f -> f
                                .filter(correlationIdFilter.apply(new CorrelationIdFilter.Config()))
                                .requestRateLimiter(rl -> rl
                                        .setRateLimiter(redisRateLimiter)
                                        .setKeyResolver(exchange -> exchange.getPrincipal().map(p -> p.getName())))
                                .circuitBreaker(cb -> cb
                                        .setName("ingestionCircuitBreaker")
                                        .setFallbackUri("forward:/fallback/ingestion")))
                        .uri("lb://ingestion-service"))

                // Anomaly & Management REST API Route
                .route("analytics-api-route", r -> r
                        .path("/api/v1/anomalies/**", "/api/v1/rules/**")
                        .filters(f -> f
                                .filter(correlationIdFilter.apply(new CorrelationIdFilter.Config()))
                                .filter(authFilter.apply(new AuthFilter.Config())))
                        .uri("lb://analytics-service"))

                // GraphQL API Route
                .route("graphql-route", r -> r
                        .path("/graphql", "/subscriptions")
                        .filters(f -> f
                                .filter(correlationIdFilter.apply(new CorrelationIdFilter.Config()))
                                .filter(authFilter.apply(new AuthFilter.Config())))
                        .uri("lb://graphql-service"))
                .build();
    }
}