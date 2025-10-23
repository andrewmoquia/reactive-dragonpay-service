package com.dragonpay.reactive_dragonpay_service.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import static org.springframework.security.config.Customizer.withDefaults;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.header.ReferrerPolicyServerHttpHeadersWriter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebFluxSecurity
public class AppSecurityConfig {
    private final AppAuthenticationManager authenticationManager;
    private final AppAuthenticationEntryPoint authenticationEntryPoint;

    public AppSecurityConfig(AppAuthenticationManager authenticationManager, AppAuthenticationEntryPoint authenticationEntryPoint) {
        this.authenticationManager = authenticationManager;
        this.authenticationEntryPoint = authenticationEntryPoint;
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http

            .csrf(ServerHttpSecurity.CsrfSpec::disable)

            .authenticationManager(authenticationManager)

            .cors(cors -> cors.configurationSource(corsConfigurationSource()))

            .headers(headers -> headers
                .contentSecurityPolicy(withDefaults())
                .frameOptions(withDefaults())
                .referrerPolicy(referrer -> referrer.policy(ReferrerPolicyServerHttpHeadersWriter.ReferrerPolicy.SAME_ORIGIN))
                .xssProtection(withDefaults())
                .cache(withDefaults())
                .permissionsPolicy(withDefaults())
                .crossOriginEmbedderPolicy(withDefaults())
                .crossOriginOpenerPolicy(withDefaults())
                .crossOriginResourcePolicy(withDefaults())
                .contentTypeOptions(withDefaults())
            )

            .authorizeExchange(exchange -> exchange
                .pathMatchers("/private/**").authenticated()
                .pathMatchers("/test/**").authenticated()
                .anyExchange().denyAll()
            )
            
            .exceptionHandling(ex -> ex.authenticationEntryPoint(authenticationEntryPoint))

            .httpBasic(httpBasic -> httpBasic.authenticationEntryPoint(authenticationEntryPoint))

            .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("*"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT"));
        config.setAllowedHeaders(List.of(
            "Content-Type",
            "Authorization"
        ));
        config.setExposedHeaders(List.of("Authorization", "Content-Type"));
        config.setAllowCredentials(true);
        config.setMaxAge(3000L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}