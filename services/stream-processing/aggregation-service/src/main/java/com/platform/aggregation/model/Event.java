package com.platform.aggregation.model;

import lombok.Data;

@Data
public class Event {
    private String eventId;
    private String type;
    private long timestamp;
}