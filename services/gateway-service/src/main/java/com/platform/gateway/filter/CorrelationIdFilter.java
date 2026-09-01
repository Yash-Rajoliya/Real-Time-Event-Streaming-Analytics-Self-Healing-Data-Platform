// services/gateway-service/src/main/java/com/platform/gateway/filter/CorrelationIdFilter.java
package com.platform.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CorrelationIdFilter extends AbstractGatewayFilterFactory<CorrelationIdFilter.Config> {

    public static final String CORRELATION_ID_HEADER = "X-Correlation-ID";

    public CorrelationIdFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            String correlationId = request.getHeaders().getFirst(CORRELATION_ID_HEADER);

            if (correlationId == null || correlationId.isBlank()) {
                correlationId = UUID.randomUUID().toString();
            }

            ServerHttpRequest modifiedRequest = request.mutate()
                    .header(CORRELATION_ID_HEADER, correlationId)
                    .build();

            exchange.getResponse().getHeaders().add(CORRELATION_ID_HEADER, correlationId);

            return chain.filter(exchange.mutate().request(modifiedRequest).build());
        };
    }

    public static class Config {
        // Custom configurations if needed
    }
}