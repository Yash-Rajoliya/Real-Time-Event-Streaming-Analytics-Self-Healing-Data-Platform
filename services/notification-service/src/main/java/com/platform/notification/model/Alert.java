package com.platform.notification.model;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Alert {
    private String message;
    private String recipient;
    private AlertType type;
    private String severity;
}