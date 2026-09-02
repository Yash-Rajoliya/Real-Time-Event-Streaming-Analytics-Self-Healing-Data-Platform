package com.platform.aggregation.processor;

import com.platform.aggregation.model.Event;
import com.platform.aggregation.model.AggregatedMetric;
import com.platform.aggregation.window.SlidingWindow;
import com.platform.aggregation.window.TumblingWindow;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class AggregationProcessor {

    private final ConcurrentHashMap<String, Long> counters = new ConcurrentHashMap<>();

    private final SlidingWindow slidingWindow = new SlidingWindow();
    private final TumblingWindow tumblingWindow = new TumblingWindow();

    public void process(Event event) {

        // Basic count aggregation
        counters.merge(event.getType(), 1L, Long::sum);

        // Window aggregations
        slidingWindow.add(event);
        tumblingWindow.add(event);

        log.debug("Processed event: {}", event.getEventId());
    }

    public AggregatedMetric snapshot() {
        return new AggregatedMetric(counters, slidingWindow.get(), tumblingWindow.get());
    }
}