package ute.udn.apigateway;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringCloudGatewayRouting {
    @Bean
    public RouteLocator configureRoute(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(r -> r.path("/api/user/v1/**")
                        .uri("lb://user")
                )
                .route(r -> r.path("/api/auth/v1/**")
                        .uri("lb://user")
                )
                .route(r -> r.path("/api/dodientu/v1/**")
                        .uri("lb://dodientu")
                )
                .build();
    }
}
