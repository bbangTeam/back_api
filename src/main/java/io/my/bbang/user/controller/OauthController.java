package io.my.bbang.user.controller;

import io.my.bbang.user.dto.SocialLoginType;
import io.my.bbang.user.payload.request.UserJoinRequest;
import io.my.bbang.user.payload.response.UserLoginResponse;
import io.my.bbang.user.service.oauth.OauthService;
import io.my.bbang.user.service.oauth.SocialOauthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/oauth")
public class OauthController {
    private final OauthService oauthService;

    @GetMapping("/{socialLoginType}")
    public void socialLogin(
            ServerHttpResponse response,
            @PathVariable(name = "socialLoginType") String socialLoginType
    ) {
        SocialOauthService socialOauthService = oauthService.findSocialOauthByType(SocialLoginType.findByName(socialLoginType));
        URI uri = URI.create(socialOauthService.getOauthRedirectURL());

        response.setStatusCode(HttpStatus.FOUND);
        response.getHeaders().setLocation(uri);
    }

    @GetMapping("/callback/{socialLoginType}")
    public Mono<ResponseEntity<Object>> callback(
            @PathVariable(name = "socialLoginType") String socialLoginType,
            @RequestParam(name = "code") String code,
            @RequestParam(name = "state", required = false) String state) {

        return oauthService
                .requestAccessToken(
                        SocialLoginType.findByName(socialLoginType), code, state)
                .flatMap(response -> {
                    String accessToken = response.getAccessToken();
                    String loginId = response.getLoginId();
                    int resultCode = response.getCode();
                    String result = response.getResult();

                    HttpHeaders httpHeaders = new HttpHeaders();
                    httpHeaders.set("accessToken", accessToken);
                    httpHeaders.set("loginId", loginId);
                    httpHeaders.set("code", String.valueOf(resultCode));
                    httpHeaders.set("result", result);

                    return Mono.just(ResponseEntity.ok().headers(httpHeaders).build());
                });
    }

    @PostMapping("/join/{socialLoginType}")
    public Mono<UserLoginResponse> join(
            @PathVariable(name = "socialLoginType") String socialLoginType,
            @RequestBody UserJoinRequest requestBody) {

        SocialLoginType type = SocialLoginType.findByName(socialLoginType);

        return oauthService.join(type, requestBody);
    }

}
