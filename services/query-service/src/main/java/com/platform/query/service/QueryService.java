// services/query-service/src/main/java/com/platform/query/service/QueryService.java
package com.platform.query.service;

import com.platform.query.controller.QueryController.ExecutionMetrics;
import com.platform.query.controller.QueryController.QueryRequest;
import com.platform.query.controller.QueryController.QueryResponse;
import com.platform.query.service.QueryPlanner.ExecutionPlan;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class QueryService {

    private final QueryPlanner queryPlanner;

    public QueryResponse executeQuery(QueryRequest request) {
        String queryId = "qry_" + UUID.randomUUID().toString().replace("-", "");
        long startTime = System.currentTimeMillis();

        try {
            log.info("Preparing query execution plan for query ID: {}", queryId);
            ExecutionPlan plan = queryPlanner.createPlan(request);

            // TODO: Delegate to underlying query engines (e.g., Presto/Trino, Elasticsearch, or PostgreSQL)
            // For now, returning mocked evaluation based on the planner
            List<Map<String, Object>> mockResults = executePlan(plan);

            long executionTime = System.currentTimeMillis() - startTime;

            return QueryResponse.builder()
                    .queryId(queryId)
                    .status("SUCCESS")
                    .data(mockResults)
                    .metrics(ExecutionMetrics.builder()
                            .executionTimeMs(executionTime)
                            .rowCount(mockResults.size())
                            .executedAt(Instant.now())
                            .build())
                    .build();

        } catch (Exception ex) {
            log.error("Failed to execute query ID {}: {}", queryId, ex.getMessage(), ex);
            return QueryResponse.builder()
                    .queryId(queryId)
                    .status("FAILED")
                    .errorMessage(ex.getMessage())
                    .data(Collections.emptyList())
                    .metrics(ExecutionMetrics.builder()
                            .executionTimeMs(System.currentTimeMillis() - startTime)
                            .rowCount(0)
                            .executedAt(Instant.now())
                            .build())
                    .build();
        }
    }

    private List<Map<String, Object>> executePlan(ExecutionPlan plan) {
        log.debug("Executing plan for dataset [{}] with strategy [{}]", plan.getTargetDataset(), plan.getStrategy());
        // Mocked result set mapping
        return List.of(
                Map.of("id", 1, "dataset", plan.getTargetDataset(), "result", "sample_data_1"),
                Map.of("id", 2, "dataset", plan.getTargetDataset(), "result", "sample_data_2")
        );
    }
}