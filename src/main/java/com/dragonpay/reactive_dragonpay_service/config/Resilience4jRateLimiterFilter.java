package com.dragonpay.reactive_dragonpay_service.config;

import java.time.Duration;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.reactor.ratelimiter.operator.RateLimiterOperator;
import reactor.core.publisher.Mono;

@Component
public class Resilience4jRateLimiterFilter implements WebFilter{

    private final RateLimiter rateLimiter;

    private static final String JSON_TEMPLATE = """
        {
            "success": false,
            "message": "Access Denied!",
            "error": "%s"
        }
    """;

    public Resilience4jRateLimiterFilter() {
        RateLimiterConfig config = RateLimiterConfig.custom()
            .limitForPeriod(10000)
            .limitRefreshPeriod(Duration.ofSeconds(60))
            .timeoutDuration(Duration.ofMillis(100))
            .build();

        this.rateLimiter = RateLimiter.of("globalLimiter", config);
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

        return chain.filter(exchange)
            .transformDeferred(RateLimiterOperator.of(rateLimiter))
            .onErrorResume(ex -> {
                exchange.getResponse().setStatusCode(HttpStatus.TOO_MANY_REQUESTS);

                String body = String.format(JSON_TEMPLATE, ex.getMessage());
                var buffer = exchange.getResponse()
                    .bufferFactory()
                    .wrap(body.getBytes());

                return exchange.getResponse().writeWith(Mono.just(buffer));
            });
    }
    
}
