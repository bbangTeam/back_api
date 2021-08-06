package io.my.bbang.user.service.oauth;

import io.my.bbang.commons.properties.OauthProperties;
import io.my.bbang.user.domain.User;
import io.my.bbang.user.payload.request.UserJoinRequest;
import io.my.bbang.user.payload.response.UserLoginResponse;
import io.my.bbang.user.payload.response.oauth.NaverLoginResponse;
import io.my.bbang.user.payload.response.oauth.NaverProfileResponse;
import io.my.bbang.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class NaverOauthService implements SocialOauthService {
    private final OauthProperties.Naver naverProperties;
    private final UserService userService;

    private static final String GRANT_TYPE = "authorization_code";
    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER = "Bearer ";

    @Override
    public String getOauthRedirectURL() {
        SecureRandom random = new SecureRandom();

        Map<String, Object> params = new HashMap<>();
        params.put("response_type", "code");
        params.put("client_id", naverProperties.getClientId());
        params.put("redirect_uri", naverProperties.getCallbackUrl());
        params.put("state", new BigInteger(130, random).toString());

        return urlPlusParams(naverProperties.getBaseUrl() + naverProperties.getLoginUri(), params);
    }

    @Override
    public Mono<UserLoginResponse> requestAccessToken(String code, String state) {

        Map<String, Object> params = new HashMap<>();
        params.put("grant_type", GRANT_TYPE);
        params.put("client_id", naverProperties.getClientId());
        params.put("client_secret", naverProperties.getClientSecret());
        params.put("code", code);
        params.put("state", state);

        WebClient webClient =
                WebClient.builder().baseUrl(naverProperties.getBaseUrl()).build();

        WebClient.ResponseSpec responseSpec = webClient.post()
                .uri(urlPlusParams(naverProperties.getTokenUri(), params))
                .retrieve();


        UserLoginResponse failResponseBody = new UserLoginResponse();
        return responseSpec.toEntity(NaverLoginResponse.class)
                .flatMap(response -> {
                    String accessToken = Objects.requireNonNull(response.getBody()).getAccessToken();
                    setFailLoginResponse(failResponseBody, accessToken);
                    return getUserInfoByAccessToken(accessToken).toEntity(NaverProfileResponse.class);
                })
                .map(responseSpecByProperties -> Objects.requireNonNull(responseSpecByProperties.getBody()).getResponse().getEmail())
                .flatMap(userService::findByEmail)
                .flatMap(userService::buildUserLoginResponseByUser)
                .switchIfEmpty(Mono.defer(() -> Mono.just(failResponseBody)))
                ;
    }

    @Override
    public Mono<UserLoginResponse> join(UserJoinRequest requestBody) {
        WebClient.ResponseSpec responseSpec = getUserInfoByAccessToken(requestBody.getAccessToken());

        return responseSpec.toEntity(NaverProfileResponse.class)
                .flatMap(response -> {
                    NaverProfileResponse body = response.getBody();
                    assert body != null;
                    User user = User.newInstance(
                            body.getResponse().getEmail(),
                            body.getResponse().getName(),
                            requestBody.getNickname(),
                            body.getResponse().getProfileImage()
                    );
                    return userService.saveUser(user);
                })
                .flatMap(userService::buildUserLoginResponseByUser)
                ;
    }

    @Override
    public WebClient.ResponseSpec getUserInfoByAccessToken(String accessToken) {
        WebClient webClient =
                WebClient.builder().baseUrl(naverProperties.getProfileUrl())
                        .build();

        return webClient.post()
                .uri(naverProperties.getProfileUri())
                .header(AUTHORIZATION, BEARER + accessToken)
                .retrieve()
                ;
    }
}
