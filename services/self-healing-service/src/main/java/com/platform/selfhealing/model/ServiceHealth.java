package com.platform.selfhealing.model;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ServiceHealth {
    private String serviceName;
    private boolean healthy;
    private String reason;
}