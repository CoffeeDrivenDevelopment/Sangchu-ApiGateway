package com.cdd.apigateway.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@Getter
@NoArgsConstructor
public class FilterConfig {
    private String message;
    private String preLogger;
    private String postLogger;
}
