package com.dragonpay.reactive_dragonpay_service.config;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import reactor.core.publisher.Mono;

@Component
public class AppAuthenticationManager implements ReactiveAuthenticationManager {
    private final PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    private final String encodedPassword = passwordEncoder.encode("12345");

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = String.valueOf(authentication.getCredentials());

        if ("admin".equals(username) && passwordEncoder.matches(password, encodedPassword)) {
            var userDetails = User.withUsername(username)
                .password(password)
                .roles("USER")
                .build();

            return Mono.just(new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities()));
        }

        return Mono.error(new BadCredentialsException("Invalid username or password!"));
    }
}
