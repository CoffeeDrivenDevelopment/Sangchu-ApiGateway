package com.cdd.apigateway.filter.passport;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PassportBody(
	@JsonProperty("token")
	String authorizationAsString
) {
}
