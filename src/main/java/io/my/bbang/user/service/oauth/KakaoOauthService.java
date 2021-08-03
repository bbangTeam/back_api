package io.my.bbang.user.service.oauth;

import io.my.bbang.commons.properties.OauthProperties;
import io.my.bbang.user.domain.User;
import io.my.bbang.user.payload.request.UserJoinRequest;
import io.my.bbang.user.payload.request.oauth.KakaoProfileRequest;
import io.my.bbang.user.payload.response.UserLoginResponse;
import io.my.bbang.user.payload.response.oauth.KakaoLoginResponse;
import io.my.bbang.user.payload.response.oauth.KakaoProfileResponse;
import io.my.bbang.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoOauthService implements SocialOauthService {
    private final OauthProperties.Kakao kakaoProperties;
    private final UserService userService;

    private static final String GRANT_TYPE = "authorization_code";
    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER = "Bearer ";

    @Override
    public String getOauthRedirectURL() {
        Map<String, Object> params = new HashMap<>();

        params.put("client_id", kakaoProperties.getClientId());
        params.put("redirect_uri", kakaoProperties.getCallbackUrl());
        params.put("response_type", "code");

        return urlPlusParams(kakaoProperties.getBaseUrl() + kakaoProperties.getLoginUri(), params);
    }

    @Override
    public Mono<UserLoginResponse> requestAccessToken(String code, String state) {
        Map<String, Object> params = new HashMap<>();
        params.put("code", code);
        params.put("client_id", kakaoProperties.getClientId());
        params.put("client_secret", kakaoProperties.getClientSecret());
        params.put("redirect_uri", kakaoProperties.getCallbackUrl());
        params.put("grant_type", GRANT_TYPE);

        WebClient webclient =
                WebClient.builder().baseUrl(kakaoProperties.getBaseUrl())
                        .build()
                ;

        WebClient.ResponseSpec responseSpec = webclient.post()
                .uri(urlPlusParams(kakaoProperties.getTokenUri(), params))
                .retrieve()
                ;

        UserLoginResponse failResponseBody = new UserLoginResponse();
        return responseSpec.toEntity(KakaoLoginResponse.class)
                .flatMap(response -> {
                    String accessToken = response.getBody().getAccessToken();
                    setFailLoginResponse(failResponseBody, accessToken);
                    return getUserInfoByAccessToken(accessToken).toEntity(KakaoProfileResponse.class);
                })
                .map(responseSpecByProperties -> responseSpecByProperties.getBody().getKakaoAcount().getEmail())
                .flatMap(userService::findByEmail)
                .flatMap(userService::buildUserLoginResponseByUser)
                .switchIfEmpty(Mono.defer(() -> Mono.just(failResponseBody)))
                ;
    }

    @Override
    public Mono<UserLoginResponse> join(UserJoinRequest requestBody) {
        WebClient.ResponseSpec responseSpec = getUserInfoByAccessToken(requestBody.getAccessToken());

        return responseSpec.toEntity(KakaoProfileResponse.class)
                .flatMap(response -> {
                    KakaoProfileResponse body = response.getBody();
                    User user = User.newInstance(
                            body.getKakaoAcount().getEmail(),
                            null,
                            requestBody.getNickname(),
                            body.getKakaoAcount().getProfile().getProfileImageUrl()
                    );
                    return userService.saveUser(user);
                })
                .flatMap(userService::buildUserLoginResponseByUser);
    }

    @Override
    public WebClient.ResponseSpec getUserInfoByAccessToken(String accessToken) {
        WebClient webClient =
                WebClient.builder().baseUrl(kakaoProperties.getProfileUrl())
                        .build();

        return webClient.post()
                .uri(kakaoProperties.getProfileUri())
                .header(AUTHORIZATION, BEARER + accessToken)
                .bodyValue(new KakaoProfileRequest())
                .retrieve()
                ;

    }


}
