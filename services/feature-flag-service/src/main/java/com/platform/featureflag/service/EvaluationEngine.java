package com.platform.featureflag.service;

import com.platform.featureflag.model.*;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Random;

@Component
public class EvaluationEngine {

    public EvaluationResponse evaluate(FeatureFlag flag, EvaluationRequest request) {

        // 1. Environment check
        if (!flag.getEnvironment().equals(request.getEnvironment())) {
            return EvaluationResponse.builder()
                    .enabled(false)
                    .reason("Environment mismatch")
                    .build();
        }

        // 2. Global toggle
        if (!flag.isEnabled()) {
            return EvaluationResponse.builder()
                    .enabled(false)
                    .reason("Feature disabled")
                    .build();
        }

        // 3. Target rules
        if (flag.getRules() != null) {
            for (TargetRule rule : flag.getRules()) {
                if (matches(rule, request.getAttributes())) {
                    return EvaluationResponse.builder()
                            .enabled(true)
                            .reason("Matched targeting rule")
                            .build();
                }
            }
        }

        // 4. % rollout (hash-based recommended in real systems)
        int bucket = Math.abs(request.getUserId().hashCode() % 100);

        if (bucket < flag.getRolloutPercentage()) {
            return EvaluationResponse.builder()
                    .enabled(true)
                    .reason("Within rollout percentage")
                    .build();
        }

        return EvaluationResponse.builder()
                .enabled(false)
                .reason("Outside rollout percentage")
                .build();
    }

    private boolean matches(TargetRule rule, Map<String, String> attributes) {

        String value = attributes.get(rule.getAttribute());

        if (value == null) return false;

        return switch (rule.getOperator()) {
            case "equals" -> value.equals(rule.getValue());
            case "contains" -> value.contains(rule.getValue());
            default -> false;
        };
    }
}