package com.platform.aggregation.window;

import com.platform.aggregation.model.Event;

import java.util.concurrent.atomic.AtomicInteger;

public class TumblingWindow {

    private final AtomicInteger counter = new AtomicInteger(0);
    private long windowStart = System.currentTimeMillis();
    private final long WINDOW_SIZE_MS = 60000;

    public void add(Event event) {

        long now = System.currentTimeMillis();

        if (now - windowStart > WINDOW_SIZE_MS) {
            counter.set(0);
            windowStart = now;
        }

        counter.incrementAndGet();
    }

    public int get() {
        return counter.get();
    }
}