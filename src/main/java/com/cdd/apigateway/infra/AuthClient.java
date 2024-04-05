package com.cdd.apigateway.infra;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.cdd.apigateway.exception.ErrorCode;
import com.cdd.apigateway.exception.GatewayException;
import com.cdd.apigateway.filter.passport.Passport;
import com.cdd.apigateway.filter.passport.PassportBody;
import com.cdd.apigateway.properties.PassportProperties;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AuthClient {
	private final String apiUri;
	private final RestTemplate restTemplate;

	@Autowired
	public AuthClient(
		PassportProperties passportProperties,
		RestTemplate restTemplate
	) {
		apiUri = passportProperties.getApiUri();
		this.restTemplate = restTemplate;
	}

	public Passport issuePassport(PassportBody body) {
		ResponseEntity<Passport> passport = restTemplate.postForEntity(
			apiUri + "/auth-service/v1/passport", body, Passport.class);
		if (passport.getStatusCode().value() == 200) {
			log.info("Issue Passport Success!");
			return passport.getBody();
		} else if (passport.getStatusCode().value() == 400) {
			log.warn("Issue Passport Fail!");
			throw new GatewayException(ErrorCode.NON_EXIST_AUTHORIZATION_HEADER);
		}
		log.error("Issue Passport Internal Server Error!");
		throw new GatewayException(ErrorCode.INTERNAL_SERVER_ERROR);
	}
}
