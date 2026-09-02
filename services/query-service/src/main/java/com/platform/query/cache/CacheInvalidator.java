// services/query-service/src/main/java/com/platform/query/cache/CacheInvalidator.java
package com.platform.query.cache;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CacheInvalidator {

    private final RedisCacheService cacheService;

    @KafkaListener(
            topics = "${app.kafka.topics.schema-updates:schema-updates-topic}", 
            groupId = "${spring.kafka.consumer.group-id:query-cache-group}"
    )
    public void handleSchemaUpdate(String subject) {
        log.info("Received schema update event for subject [{}]. Invalidating target cache entries.", subject);
        String pattern = "gql_query:*" + subject + "*";
        cacheService.evictByPattern(pattern);
    }

    @KafkaListener(
            topics = "${app.kafka.topics.dataset-updates:dataset-updates-topic}", 
            groupId = "${spring.kafka.consumer.group-id:query-cache-group}"
    )
    public void handleDatasetUpdate(String dataset) {
        log.info("Received dataset state change event for [{}]. Evicting stale query cache.", dataset);
        String pattern = "gql_query:*" + dataset + "*";
        cacheService.evictByPattern(pattern);
    }
}