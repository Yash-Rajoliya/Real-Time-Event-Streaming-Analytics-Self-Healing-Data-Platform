package com.platform.audit.model;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuditLog {

    private String id;
    private String service;
    private String userId;
    private String action;
    private String metadata;
    private AuditType type;
    private long timestamp;
}