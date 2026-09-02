package com.platform.aggregation.window;

import com.platform.aggregation.model.Event;

import java.util.LinkedList;
import java.util.Queue;

public class SlidingWindow {

    private final Queue<Event> window = new LinkedList<>();
    private final long WINDOW_SIZE_MS = 60000; // 1 min

    public void add(Event event) {

        long now = System.currentTimeMillis();
        window.add(event);

        while (!window.isEmpty() &&
                (now - window.peek().getTimestamp()) > WINDOW_SIZE_MS) {
            window.poll();
        }
    }

    public int get() {
        return window.size();
    }
}