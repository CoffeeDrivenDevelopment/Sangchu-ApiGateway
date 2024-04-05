package com.cdd.apigateway.exception;

public record ErrorResponse(
		int statusCode,
		String errorCode,
		String message
) {

	public static ErrorResponse from(ErrorCode errorCode) {
		return new ErrorResponse(
				errorCode.getStatusCode(),
				errorCode.getErrorCode(),
				errorCode.getMessage()
		);
	}

	public static ErrorResponse of(int statusCode, String errorCode, String message) {
		return new ErrorResponse(
				statusCode,
				errorCode,
				message
		);
	}
}
