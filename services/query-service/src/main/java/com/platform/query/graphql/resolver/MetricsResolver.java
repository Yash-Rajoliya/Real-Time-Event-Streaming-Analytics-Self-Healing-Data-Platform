// services/query-service/src/main/java/com/platform/query/graphql/resolver/MetricsResolver.java
package com.platform.query.graphql.resolver;

import com.platform.query.cache.RedisCacheService;
import com.platform.query.controller.QueryController.ExecutionMetrics;
import com.platform.query.controller.QueryController.QueryResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MetricsResolver {

    private final RedisCacheService cacheService;

    @SchemaMapping(typeName = "QueryResponse", field = "metrics")
    public ExecutionMetrics getMetrics(QueryResponse response) {
        log.debug("Resolving nested execution metrics for query ID: {}", response.getQueryId());
        
        if (response.getMetrics() != null) {
            return response.getMetrics();
        }

        // Cache fallback lookup for standalone metrics tracking
        String metricsCacheKey = cacheService.generateKey("metrics", response.getQueryId());
        return cacheService.get(metricsCacheKey, ExecutionMetrics.class);
    }
}