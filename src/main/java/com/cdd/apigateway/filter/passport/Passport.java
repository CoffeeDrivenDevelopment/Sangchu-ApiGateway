package com.cdd.apigateway.filter.passport;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Passport(
		@JsonProperty("passport_id")
		String id,
		@JsonProperty("member_id")
		int memberId,
		@JsonProperty("expired_time")
		long expiredTime
) {
}
