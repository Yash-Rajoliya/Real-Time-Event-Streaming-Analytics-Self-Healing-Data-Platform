// services/ingestion-service/src/main/java/com/platform/ingestion/validation/SchemaValidator.java
package com.platform.ingestion.validation;

import com.platform.ingestion.controller.EventController.EventPayload;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Set;

@Component
public class SchemaValidator {

    private static final Set<String> ALLOWED_EVENT_TYPES = Set.of(
            "user.signup", "user.login", "page.view", "order.created", "telemetry.ping", "anomaly.triggered"
    );

    public Mono<EventPayload> validatePayloadSchema(EventPayload payload) {
        return Mono.fromCallable(() -> {
            if (!ALLOWED_EVENT_TYPES.contains(payload.getEventType())) {
                throw new IllegalArgumentException("Unsupported event_type: " + payload.getEventType());
            }

            Map<String, Object> data = payload.getData();
            if (data == null || data.isEmpty()) {
                throw new IllegalArgumentException("Event payload data map cannot be null or empty");
            }

            return payload;
        });
    }
}