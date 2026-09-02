// services/query-service/src/main/java/com/platform/query/graphql/resolver/QueryResolver.java
package com.platform.query.graphql.resolver;

import com.platform.query.cache.RedisCacheService;
import com.platform.query.controller.QueryController.QueryRequest;
import com.platform.query.controller.QueryController.QueryResponse;
import com.platform.query.service.QueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.time.Duration;
import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
public class QueryResolver {

    private final QueryService queryService;
    private final RedisCacheService cacheService;

    @QueryMapping
    public QueryResponse executeQuery(
            @Argument String statement, 
            @Argument String dataset, 
            @Argument Map<String, Object> parameters) {

        String cacheKey = cacheService.generateKey("gql_query", dataset, statement, String.valueOf(parameters));
        QueryResponse cachedResponse = cacheService.get(cacheKey, QueryResponse.class);

        if (cachedResponse != null) {
            log.debug("Serving GraphQL query execution response from Redis cache for dataset [{}]", dataset);
            return cachedResponse;
        }

        QueryRequest request = new QueryRequest(statement, dataset, parameters);
        QueryResponse response = queryService.executeQuery(request);

        if ("SUCCESS".equals(response.getStatus())) {
            cacheService.put(cacheKey, response, Duration.ofMinutes(5));
        }

        return response;
    }
}