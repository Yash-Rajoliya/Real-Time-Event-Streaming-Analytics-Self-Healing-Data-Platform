package com.platform.featureflag.model;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TargetRule {

    private String attribute; // country, userId, plan
    private String operator;  // equals, contains
    private String value;
}