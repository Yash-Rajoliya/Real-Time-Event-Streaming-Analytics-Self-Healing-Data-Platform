package com.platform.selfhealing.model;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LagMetrics {
    private String topic;
    private String consumerGroup;
    private long lag;
}