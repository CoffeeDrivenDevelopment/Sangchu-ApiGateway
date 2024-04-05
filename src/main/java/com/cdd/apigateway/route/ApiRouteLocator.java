package com.cdd.apigateway.route;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.GatewayFilterSpec;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.gateway.route.builder.UriSpec;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cdd.apigateway.config.FilterConfig;
import com.cdd.apigateway.filter.authorization.AuthorizationFilter;
import com.cdd.apigateway.filter.passport.PassportFilter;
import com.cdd.apigateway.properties.LoadBalanceProperties;
import com.cdd.apigateway.properties.ServiceProperties;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class ApiRouteLocator {

	private final PassportFilter passportFilter;
	private final AuthorizationFilter authorizationFilter;
	private final LoadBalanceProperties loadBalanceProperties;
	private final ServiceProperties serviceProperties;

	@Bean
	public RouteLocator routes(RouteLocatorBuilder builder) {
		return builder.routes()
			/* Auth Service */
			.route("auth-service-auth",
				r -> r.path(serviceProperties.getAuth() + "/v1/logout")
					.filters(this::passportFilter)
					.uri(loadBalanceProperties.getAuth()))
			.route("auth-service-passport",
				r -> r.path(serviceProperties.getAuth() + "/v1/passport")
					.filters(this::authorizationFilter)
					.uri(loadBalanceProperties.getAuth()))
			.route("auth-service",
				r -> r.path(serviceProperties.getAuth() + "/**")
					.uri(loadBalanceProperties.getAuth()))
			/* Member Service */
			.route("member-service-signin",
				r -> r.path(serviceProperties.getMember() + "/v1/signin")
					.uri(loadBalanceProperties.getMember()))
			.route("member-service",
				r -> r.path(serviceProperties.getMember() + "/**")
					.filters(this::passportFilter)
					.uri(loadBalanceProperties.getMember()))
			/* Notification */
			.route("notification-service",
				r -> r.path(serviceProperties.getNotification() + "/**")
					.filters(this::passportFilter)
					.uri(loadBalanceProperties.getNotification()))
			/* Recipe Service */
			.route("recipe-service",
				r -> r.path(serviceProperties.getRecipeIngredient() + "/**")
					.filters(this::passportFilter)
					.uri(loadBalanceProperties.getRecipeIngredient()))
			/* Storage Service */
			.route("storage-service",
				r -> r.path(serviceProperties.getStorage() + "/**")
					.filters(this::passportFilter)
					.uri(loadBalanceProperties.getStorage()))
			.build();
	}

	private UriSpec authorizationFilter(GatewayFilterSpec f) {
		return f.filters(authorizationFilter.apply(new FilterConfig()));
	}

	private UriSpec passportFilter(GatewayFilterSpec f) {
		return f.filters(passportFilter.apply(new FilterConfig()));
	}
}
