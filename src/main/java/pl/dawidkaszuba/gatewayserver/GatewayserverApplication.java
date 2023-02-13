package pl.dawidkaszuba.gatewayserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class GatewayserverApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayserverApplication.class, args);
	}


	@Bean
	public RouteLocator myRoutes(RouteLocatorBuilder builder) {
		return builder.routes()
				.route(p -> p
						.path("/homebudget/budget/**")
						.filters(f -> f
								.rewritePath("/homebudget/budget/(?<segment>.*)","/${segment}")
								.removeRequestHeader("Cookie"))
						.uri("lb://BUDGETSERVICE"))
				.route(p -> p
						.path("/homebudget/budget/**")
						.filters(f -> f
								.rewritePath("/homebudget/report/(?<segment>.*)","/${segment}")
								.removeRequestHeader("Cookie"))
						.uri("lb://REPORTSERVICE"))
				.build();
	}
}
