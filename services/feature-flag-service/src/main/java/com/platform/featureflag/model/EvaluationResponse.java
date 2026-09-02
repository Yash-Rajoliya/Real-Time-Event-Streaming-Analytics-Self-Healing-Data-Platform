package com.platform.featureflag.model;

import lombok.*;

@Data
@Builder
public class EvaluationResponse {
    private boolean enabled;
    private String reason;
}