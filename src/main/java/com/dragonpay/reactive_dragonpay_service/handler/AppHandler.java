package com.dragonpay.reactive_dragonpay_service.handler;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.dragonpay.reactive_dragonpay_service.common.ApiResponse;

import reactor.core.publisher.Mono;

@Component
public class AppHandler {

    private static final Logger logger = LoggerFactory.getLogger(AppHandler.class);

    public Mono<ServerResponse> getUser(ServerRequest request) {
        logger.info("Body: {}", request.queryParams());
        Map<String, Object> user = new HashMap<>();
        user.put("name", "test");

        return ServerResponse.ok().bodyValue(ApiResponse.success("Test success", user));
    }
}
