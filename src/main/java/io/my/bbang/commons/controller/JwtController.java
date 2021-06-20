package io.my.bbang.commons.controller;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.my.bbang.commons.payloads.BbangResponse;
import io.my.bbang.commons.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequiredArgsConstructor
public class JwtController {
    private final JwtUtil jwtUtil;

    @GetMapping("/api/jwt/issued")
    public Mono<BbangResponse> accessTokenIssued(ServerHttpRequest request, ServerHttpResponse response){
        log.info("call access token issued api!");
        String accessToken = request.getHeaders().get("Authorization").get(0).substring(7);

        String id = jwtUtil.parseAccessToken(accessToken).getId();
        
        String responseAccessToken = jwtUtil.createAccessToken(id);
        response.getHeaders().add("Authorization", responseAccessToken);
        
        return Mono.just(new BbangResponse("Success"));
    }
    
}
