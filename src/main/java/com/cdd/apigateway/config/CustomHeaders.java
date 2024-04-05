package com.cdd.apigateway.config;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum CustomHeaders {
    PASSPORT("Sangchu-Passport"),
    ;
    private final String headerName;
}
