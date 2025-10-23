package com.dragonpay.reactive_dragonpay_service.router;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import com.dragonpay.reactive_dragonpay_service.handler.AppHandler;

@Configuration
public class AppRouterConfig {

    @Bean
    public RouterFunction<ServerResponse> testRoutes(AppHandler handler) {
        return route(GET("/private"), handler::getUser);
    }

    @Bean
    public RouterFunction<ServerResponse> routes(AppHandler handler) {
        return testRoutes(handler);
    }
}
