package com.cdd.apigateway.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "service")
public class ServiceProperties {
	private String baseUrl;
	private String auth;
	private String member;
	private String recipeIngredient;
	private String notification;
	private String storage;
}