package com.cdd.apigateway.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "passport")
public class PassportProperties {
	private String apiUri;
}
