package com.platform.featureflag.service;

import com.platform.featureflag.cache.FeatureCache;
import com.platform.featureflag.model.*;
import com.platform.featureflag.repository.FeatureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FeatureService {

    private final FeatureRepository repository;
    private final FeatureCache cache;
    private final EvaluationEngine engine;

    public EvaluationResponse evaluate(EvaluationRequest request) {

        FeatureFlag flag = cache.get(request.getFeatureKey());

        if (flag == null) {
            flag = repository.findByKey(request.getFeatureKey());
            cache.put(flag);
        }

        return engine.evaluate(flag, request);
    }
}