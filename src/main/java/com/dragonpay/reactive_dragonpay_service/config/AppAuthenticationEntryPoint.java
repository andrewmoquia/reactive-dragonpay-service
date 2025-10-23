package com.dragonpay.reactive_dragonpay_service.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Component
public class AppAuthenticationEntryPoint implements ServerAuthenticationEntryPoint {

    @Override
    public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException ex) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

        String body =  """
            {
                "success": false,
                "message": "Access Denied!",
                "error": "%s"
            }
        """.formatted(ex.getMessage());

        var buffer = exchange.getResponse()
            .bufferFactory()
            .wrap(body.getBytes());

        return exchange.getResponse().writeWith(Mono.just(buffer));
    }
    
}
