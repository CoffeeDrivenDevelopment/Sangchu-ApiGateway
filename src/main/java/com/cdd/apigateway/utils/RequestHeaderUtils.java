package com.cdd.apigateway.utils;

import org.springframework.http.HttpHeaders;
import org.springframework.web.server.ServerWebExchange;

import com.cdd.apigateway.config.CustomHeaders;

public class RequestHeaderUtils {

	private RequestHeaderUtils() {
	}

	public static void addHeader(ServerWebExchange exchange, CustomHeaders customHeader, String value) {
		HttpHeaders httpHeaders = HttpHeaders.writableHttpHeaders(exchange.getRequest().getHeaders());
		httpHeaders.add(customHeader.getHeaderName(), value);
		exchange.getRequest().mutate().headers(
			headers -> headers.addAll(new HttpHeaders())
		);
	}
}
