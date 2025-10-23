package com.dragonpay.reactive_dragonpay_service.controller;

import java.util.Map;
import java.util.HashMap;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

import org.springframework.web.bind.annotation.GetMapping;

import com.dragonpay.reactive_dragonpay_service.common.ApiResponse;

@RestController
@RequestMapping("/test")
public class AppController {

    @GetMapping
    public Mono<ResponseEntity<ApiResponse<Map<String, Object>>>> testEndpoint() {

        try {
            Map<String, Object> user = new HashMap<>();
            user.put("age", 22);

            return Mono.just(ResponseEntity
                .ok()
                .body(ApiResponse.success("Congrats", user))
            );
            
        } catch (IllegalArgumentException e) {
            return Mono.just(ResponseEntity
                .internalServerError()
                .body(ApiResponse.error(e.getLocalizedMessage()))
            );
        }
    }
}
