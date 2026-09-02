package com.platform.aggregation.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class AggregatedMetric {

    private Map<String, Long> counts;
    private int slidingWindowCount;
    private int tumblingWindowCount;
}