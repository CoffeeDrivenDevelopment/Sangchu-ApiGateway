package com.cdd.apigateway.filter.passport;

import java.util.Objects;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import com.cdd.apigateway.config.CustomHeaders;
import com.cdd.apigateway.config.FilterConfig;
import com.cdd.apigateway.exception.ErrorCode;
import com.cdd.apigateway.exception.GatewayException;
import com.cdd.apigateway.infra.AuthClient;
import com.cdd.apigateway.utils.RequestHeaderUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Component
public class PassportFilter extends AbstractGatewayFilterFactory<FilterConfig> {
	private final ObjectMapper mapper;
	private final AuthClient authClient;

	@Override
	public GatewayFilter apply(FilterConfig config) {
		return ((exchange, chain) -> {
			ServerHttpRequest request = exchange.getRequest();

			if (request.getHeaders().containsKey(CustomHeaders.PASSPORT.getHeaderName())) {
				return chain.filter(exchange);
			}

			if (validateContainsAuthorizationHeader(request.getHeaders())) {
				log.warn("Validate Authorization Header Error!");
				throw new GatewayException(ErrorCode.NON_EXIST_AUTHORIZATION_HEADER);
			}

			Passport passport = authClient.issuePassport(
				new PassportBody(Objects.requireNonNull(request.getHeaders().get("Authorization"))
					.toString().substring(7)));

			try {
				String passportAsString = mapper.writeValueAsString(passport);
				log.info("Passport As String : [{}]", passportAsString);
				RequestHeaderUtils.addHeader(
					exchange,
					CustomHeaders.PASSPORT,
					passportAsString
				);
				return chain.filter(exchange);
			} catch (JsonProcessingException e) {
				throw new GatewayException(ErrorCode.INTERNAL_SERVER_ERROR);
			}
		});
	}

	private boolean validateContainsAuthorizationHeader(HttpHeaders headers) {
		return !headers.containsKey(HttpHeaders.AUTHORIZATION)
			|| !Objects.requireNonNull(headers.get(HttpHeaders.AUTHORIZATION)).get(0).startsWith("Bearer ");
	}
}
