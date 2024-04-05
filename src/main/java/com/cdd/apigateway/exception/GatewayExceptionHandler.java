package com.cdd.apigateway.exception;

import java.util.Objects;

import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.Ordered;
import org.springframework.core.ResolvableType;
import org.springframework.core.annotation.Order;
import org.springframework.core.codec.Hints;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.cdd.common.exception.SangChuException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RequiredArgsConstructor
@Component
public class GatewayExceptionHandler implements ErrorWebExceptionHandler {
	private final ObjectMapper mapper;

	@Override
	public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
		ServerHttpResponse response = exchange.getResponse();
		response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
		response.setStatusCode(getHttpStatus(ex));

		ErrorResponse errorResponse = getErrorResponse(ex);
		return response.writeWith(
				new Jackson2JsonEncoder(mapper).encode(
						Mono.just(Objects.requireNonNull(errorResponse)),
						response.bufferFactory(),
						ResolvableType.forInstance(errorResponse),
						MediaType.APPLICATION_JSON,
						Hints.from(Hints.LOG_PREFIX_HINT, exchange.getLogPrefix())
				)
		);
	}

	private ErrorResponse getErrorResponse(Throwable ex) {
		if (ex instanceof GatewayException gatewayException) {
			return ErrorResponse.from(gatewayException.getErrorCode());
		}

		if (ex instanceof SangChuException sangchuException) {
			return ErrorResponse.of(
					sangchuException.getStatusCode(),
					sangchuException.getErrorCode(),
					sangchuException.getMessage()
			);
		}
		return ErrorResponse.from(ErrorCode.INTERNAL_SERVER_ERROR);
	}

	private HttpStatus getHttpStatus(Throwable ex) {
		if (ex instanceof GatewayException gatewayException) {
			return getHttpStatus(gatewayException);
		}
		if (ex instanceof SangChuException sangChuException) {
			return getHttpStatus(sangChuException);
		}
		return HttpStatus.INTERNAL_SERVER_ERROR;
	}

	private HttpStatus getHttpStatus(GatewayException gatewayException) {
		for (HttpStatus status : HttpStatus.values()) {
			if (status.value() == gatewayException.getErrorCode().getStatusCode()) {
				return status;
			}
		}
		return HttpStatus.INTERNAL_SERVER_ERROR;
	}

	private HttpStatus getHttpStatus(SangChuException sangChuException) {
		for (HttpStatus status : HttpStatus.values()) {
			if (status.value() == sangChuException.getStatusCode()) {
				return status;
			}
		}
		return HttpStatus.INTERNAL_SERVER_ERROR;
	}
}
