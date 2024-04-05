package com.cdd.apigateway.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    NON_EXIST_AUTHORIZATION_HEADER(400, "HEADER_01", "AUTHORIZATION 헤더가 존재하지 않습니다."),
    INTERNAL_SERVER_ERROR(500, "SERVER_02", "알 수 없는 내부 서버 오류입니다."),
    ;

    private final int statusCode;
    private final String errorCode;
    private final String message;
}
