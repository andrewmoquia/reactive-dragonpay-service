package com.dragonpay.reactive_dragonpay_service.config;

import java.time.Duration;
import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import reactor.core.publisher.Mono;

@Component
public class LoggingWebFilter implements WebFilter{
    private static final Logger log = LoggerFactory.getLogger(LoggingWebFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        Instant start = Instant.now();
        String method = exchange.getRequest().getMethod().toString();
        String path = exchange.getRequest().getURI().getPath();
        String routeInfo = method + " " + path;

        log.info("[REQUEST] [{}] at {}", routeInfo, start);

        return chain.filter(exchange)
            .doOnSuccess(_ -> {
                long duration = Duration.between(start, Instant.now()).toMillis();
                log.info("[RESPONSE] [{}] completed in {}ms", routeInfo, duration);
            })
            .doOnError(error -> {
                long duration = Duration.between(start, Instant.now()).toMillis();
                log.info("[ERROR] [{}] after {}ms | Exception: {}", routeInfo, duration, error.toString(), error);
            });
    }
    
}
