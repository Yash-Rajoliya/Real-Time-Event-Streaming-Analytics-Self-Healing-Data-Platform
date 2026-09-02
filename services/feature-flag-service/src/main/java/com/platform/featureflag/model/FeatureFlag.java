package com.platform.featureflag.model;

import lombok.*;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FeatureFlag {

    private String key;
    private boolean enabled;

    private int rolloutPercentage; // 0–100

    private List<TargetRule> rules;

    private String environment; // prod, staging
}