package io.my.bbang.commons.controller;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.my.bbang.commons.payloads.BbangResponse;
import io.my.bbang.commons.service.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequiredArgsConstructor
public class JwtController {
    private final JwtService jwtService;

    @GetMapping(value = "/api/jwt/issued", produces = "application/json")
    public Mono<BbangResponse> accessTokenIssued(ServerHttpRequest request, ServerHttpResponse response){
        log.info("call access token issued api!");
        String accessToken = request.getHeaders().get("Authorization").get(0).substring(7);

        response.getHeaders().add("Authorization", jwtService.accessTokenIssued(accessToken));
        
        return Mono.just(new BbangResponse("Success"));
    }
    
}
