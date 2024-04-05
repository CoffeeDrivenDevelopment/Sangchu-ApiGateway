package com.cdd.apigateway.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "service.load-balance")
public class LoadBalanceProperties {
	private String auth;
	private String member;
	private String recipeIngredient;
	private String notification;
	private String storage;
}
