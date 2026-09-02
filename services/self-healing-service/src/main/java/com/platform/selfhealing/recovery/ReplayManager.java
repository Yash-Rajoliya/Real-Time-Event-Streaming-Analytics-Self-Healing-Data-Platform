package com.platform.selfhealing.recovery;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ReplayManager {

    public void replay(String topic, String group) {
        log.warn("Replaying events for topic {} group {}", topic, group);
        // Seek offsets / replay
    }
}