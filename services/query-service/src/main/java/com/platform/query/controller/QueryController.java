// services/query-service/src/main/java/com/platform/query/controller/QueryController.java
package com.platform.query.controller;

import com.platform.query.service.QueryService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/query")
@RequiredArgsConstructor
public class QueryController {

    private final QueryService queryService;

    @PostMapping("/execute")
    public ResponseEntity<QueryResponse> executeQuery(@Valid @RequestBody QueryRequest request) {
        QueryResponse response = queryService.executeQuery(request);
        return ResponseEntity.ok(response);
    }

    // --- DTO Contracts ---

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QueryRequest {
        @NotBlank(message = "Query statement cannot be empty")
        private String statement;

        @NotNull(message = "Dataset target is required")
        private String dataset;

        private Map<String, Object> parameters;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QueryResponse {
        private String queryId;
        private String status;
        private List<Map<String, Object>> data;
        private ExecutionMetrics metrics;
        private String errorMessage;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ExecutionMetrics {
        private long executionTimeMs;
        private int rowCount;
        private Instant executedAt;
    }
}