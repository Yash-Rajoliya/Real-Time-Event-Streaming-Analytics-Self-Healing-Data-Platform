package com.platform.config.model;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConfigVersion {
    private String key;
    private int version;
}