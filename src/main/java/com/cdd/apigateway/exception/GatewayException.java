package com.cdd.apigateway.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class GatewayException extends RuntimeException {
    private final ErrorCode errorCode;
}
