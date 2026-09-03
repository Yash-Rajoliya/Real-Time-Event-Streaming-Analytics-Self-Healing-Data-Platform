// services/storage-connector/src/main/java/com/platform/storage/sink/ElasticsearchSinkConnector.java
package com.platform.storage.sink;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.bulk.BulkResponseItem;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ElasticsearchSinkConnector {

    private final ElasticsearchClient esClient;

    @Value("${storage.elasticsearch.default-index:platform-events}")
    private String defaultIndex;

    public void indexBatch(String targetIndex, List<Map<String, Object>> documents) {
        if (documents == null || documents.isEmpty()) {
            return;
        }

        String indexName = (targetIndex != null && !targetIndex.isBlank()) ? targetIndex : defaultIndex;
        BulkRequest.Builder bulkBuilder = new BulkRequest.Builder();

        for (Map<String, Object> doc : documents) {
            bulkBuilder.operations(op -> op
                    .index(idx -> idx
                            .index(indexName)
                            .document(doc)
                    )
            );
        }

        try {
            log.info("Flushing batch of {} records to Elasticsearch index [{}]", documents.size(), indexName);
            BulkResponse response = esClient.bulk(bulkBuilder.build());

            if (response.errors()) {
                log.error("Bulk indexing completed with errors for index [{}]", indexName);
                for (BulkResponseItem item : response.items()) {
                    if (item.error() != null) {
                        log.error("Error indexing document ID [{}]: {}", item.id(), item.error().reason());
                    }
                }
            } else {
                log.info("Successfully indexed {} documents into [{}] in {}ms", 
                        documents.size(), indexName, response.took());
            }
        } catch (IOException e) {
            log.error("Failed to execute bulk indexing to Elasticsearch index [{}]: {}", indexName, e.getMessage(), e);
            throw new RuntimeException("Elasticsearch bulk insert failure", e);
        }
    }
}