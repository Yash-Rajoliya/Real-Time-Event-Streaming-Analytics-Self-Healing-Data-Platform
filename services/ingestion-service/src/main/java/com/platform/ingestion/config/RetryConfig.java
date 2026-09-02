// services/ingestion-service/src/main/java/com/platform/ingestion/config/RetryConfig.java
package com.platform.ingestion.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.util.retry.Retry;

import java.time.Duration;

@Slf4j
@Configuration
public class RetryConfig {

    @Bean
    public Retry kafkaProducerRetrySpec() {
        return Retry.backoff(3, Duration.ofMillis(100))
                .maxBackoff(Duration.ofSeconds(2))
                .jitter(0.5)
                .filter(throwable -> !(throwable instanceof IllegalArgumentException))
                .doBeforeRetry(retrySignal -> log.warn("Retrying Kafka publishing attempt #{} due to: {}",
                        retrySignal.totalRetries() + 1, retrySignal.failure().getMessage()));
    }
}