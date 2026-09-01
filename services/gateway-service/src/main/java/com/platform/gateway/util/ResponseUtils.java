// services/gateway-service/src/main/java/com/platform/gateway/util/ResponseUtils.java
package com.platform.gateway.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * Utility for rendering standardized JSON error responses reactively across Spring Cloud Gateway filters.
 */
public final class ResponseUtils {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private ResponseUtils() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    public static Mono<Void> buildErrorResponse(ServerWebExchange exchange, HttpStatus status, String message, String errorCode) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(status);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        String correlationId = exchange.getRequest().getHeaders().getFirst("X-Correlation-ID");

        Map<String, Object> errorAttributes = Map.of(
                "timestamp", System.currentTimeMillis(),
                "status", status.value(),
                "error", status.getReasonPhrase(),
                "message", message,
                "code", errorCode,
                "path", exchange.getRequest().getURI().getPath(),
                "correlationId", correlationId != null ? correlationId : ""
        );

        byte[] bytes;
        try {
            bytes = OBJECT_MAPPER.writeValueAsString(errorAttributes).getBytes(StandardCharsets.UTF_8);
        } catch (JsonProcessingException e) {
            bytes = "{\"error\":\"Internal Server Error\"}".getBytes(StandardCharsets.UTF_8);
        }

        DataBuffer buffer = response.bufferFactory().wrap(bytes);
        return response.writeWith(Mono.just(buffer));
    }
}