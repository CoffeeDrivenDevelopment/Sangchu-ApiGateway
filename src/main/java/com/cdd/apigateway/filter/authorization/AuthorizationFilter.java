package com.cdd.apigateway.filter.authorization;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;

import com.cdd.apigateway.config.FilterConfig;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class AuthorizationFilter extends AbstractGatewayFilterFactory<FilterConfig> {

	@Override
	public GatewayFilter apply(FilterConfig config) {
		return (exchange, chain) -> chain.filter(exchange);
	}
}
