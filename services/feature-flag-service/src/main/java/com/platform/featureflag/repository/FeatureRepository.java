package com.platform.featureflag.repository;

import com.platform.featureflag.model.FeatureFlag;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class FeatureRepository {

    public FeatureFlag findByKey(String key) {

        return FeatureFlag.builder()
                .key(key)
                .enabled(true)
                .rolloutPercentage(50)
                .environment("prod")
                .rules(List.of())
                .build();
    }
}