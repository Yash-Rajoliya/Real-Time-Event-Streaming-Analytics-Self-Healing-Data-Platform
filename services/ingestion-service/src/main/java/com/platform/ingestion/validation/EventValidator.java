// services/ingestion-service/src/main/java/com/platform/ingestion/validation/EventValidator.java
package com.platform.ingestion.validation;

import com.platform.ingestion.controller.EventController.EventPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.Instant;

@Component
@RequiredArgsConstructor
public class EventValidator {

    private final SchemaValidator schemaValidator;
    private static final Duration MAX_FUTURE_SKEW = Duration.ofMinutes(5);
    private static final Duration MAX_PAST_SKEW = Duration.ofDays(7);

    public Mono<EventPayload> validate(EventPayload payload) {
        return Mono.fromCallable(() -> {
            Instant now = Instant.now();
            Instant eventTime = payload.getTimestamp();

            if (eventTime.isAfter(now.plus(MAX_FUTURE_SKEW))) {
                throw new IllegalArgumentException("Event timestamp is too far in the future");
            }

            if (eventTime.isBefore(now.minus(MAX_PAST_SKEW))) {
                throw new IllegalArgumentException("Event timestamp is older than max allowed retention threshold (7 days)");
            }

            return payload;
        }).flatMap(schemaValidator::validatePayloadSchema);
    }
}