package com.platform.featureflag.model;

import lombok.*;
import java.util.Map;

@Data
public class EvaluationRequest {

    private String featureKey;
    private String userId;
    private Map<String, String> attributes;
    private String environment;
}