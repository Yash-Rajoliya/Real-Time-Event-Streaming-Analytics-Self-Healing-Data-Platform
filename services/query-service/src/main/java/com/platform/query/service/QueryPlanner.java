// services/query-service/src/main/java/com/platform/query/service/QueryPlanner.java
package com.platform.query.service;

import com.platform.query.controller.QueryController.QueryRequest;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class QueryPlanner {

    public ExecutionPlan createPlan(QueryRequest request) {
        log.debug("Parsing query statement: {}", request.getStatement());

        // Standardize statement
        String normalizedStatement = request.getStatement().trim().toUpperCase();

        // Very basic validation/parsing logic for the base service
        if (!normalizedStatement.startsWith("SELECT") && !normalizedStatement.startsWith("FIND")) {
            throw new IllegalArgumentException("Only SELECT or FIND queries are supported.");
        }

        QueryStrategy strategy = determineStrategy(request.getDataset());

        return ExecutionPlan.builder()
                .targetDataset(request.getDataset())
                .optimizedStatement(normalizedStatement)
                .parameters(request.getParameters())
                .strategy(strategy)
                .build();
    }

    private QueryStrategy determineStrategy(String dataset) {
        if (dataset.toLowerCase().contains("events") || dataset.toLowerCase().contains("metrics")) {
            return QueryStrategy.ELASTICSEARCH_DIRECT;
        }
        if (dataset.toLowerCase().contains("users") || dataset.toLowerCase().contains("config")) {
            return QueryStrategy.RELATIONAL_DB;
        }
        return QueryStrategy.FEDERATED;
    }

    @Data
    @Builder
    public static class ExecutionPlan {
        private String targetDataset;
        private String optimizedStatement;
        private Map<String, Object> parameters;
        private QueryStrategy strategy;
    }

    public enum QueryStrategy {
        ELASTICSEARCH_DIRECT,
        RELATIONAL_DB,
        FEDERATED
    }
}