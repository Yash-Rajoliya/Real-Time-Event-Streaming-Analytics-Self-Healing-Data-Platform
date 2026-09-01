// services/gateway-service/src/main/java/com/platform/gateway/filter/LoggingFilter.java
package com.platform.gateway.filter;

import org.slf me.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class LoggingFilter implements GlobalFilter, Ordered {

    private static final Logger log = LoggerFactory.getLogger(LoggingFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        long startTime = System.currentTimeMillis();
        String path = exchange.getRequest().getURI().getPath();
        String method = exchange.getRequest().getMethod().name();
        String correlationId = exchange.getRequest().getHeaders().getFirst("X-Correlation-ID");

        log.info("Incoming Request: [Method: {}] [Path: {}] [CorrelationId: {}]", method, path, correlationId);

        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            long duration = System.currentTimeMillis() - startTime;
            int statusCode = exchange.getResponse().getStatusCode() != null 
                    ? exchange.getResponse().getStatusCode().value() : 500;
            
            log.info("Response Sent: [Method: {}] [Path: {}] [Status: {}] [Duration: {}ms] [CorrelationId: {}]",
                    method, path, statusCode, duration, correlationId);
        }));
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}