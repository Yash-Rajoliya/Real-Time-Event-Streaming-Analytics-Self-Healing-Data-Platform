// services/analytics-service/src/main/java/com/platform/analytics/repository/ElasticRepository.java
package com.platform.analytics.repository;

import com.platform.analytics.model.Metric;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface ElasticRepository extends ElasticsearchRepository<Metric, String> {

    List<Metric> findByMetricNameAndTimestampBetween(String metricName, Instant startTime, Instant endTime);

    List<Metric> findByTimestampBetween(Instant startTime, Instant endTime);

    List<Metric> findByCategoryAndTimestampBetween(String category, Instant startTime, Instant endTime);
}