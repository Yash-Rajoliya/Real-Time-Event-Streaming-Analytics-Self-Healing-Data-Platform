package com.platform.audit.model;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuditEvent {

    private String service;
    private String userId;
    private AuditType type;
    private String action;
    private String metadata;
    private long timestamp;
}