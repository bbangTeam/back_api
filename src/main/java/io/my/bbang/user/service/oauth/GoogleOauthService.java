package io.my.bbang.user.service.oauth;

import io.my.bbang.commons.properties.OauthProperties;
import io.my.bbang.user.domain.User;
import io.my.bbang.user.payload.request.UserJoinRequest;
import io.my.bbang.user.payload.response.UserLoginResponse;
import io.my.bbang.user.payload.response.oauth.GoogleLoginResponse;
import io.my.bbang.user.payload.response.oauth.GoogleProfileResponse;
import io.my.bbang.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class GoogleOauthService implements SocialOauthService {
    private final OauthProperties.Google googleProperties;
    private final UserService userService;

    private static final String GRANT_TYPE = "authorization_code";

    @Override
    public String getOauthRedirectURL() {
        Map<String, Object> params = new HashMap<>();

        params.put("scope", "openid%20profile%20email");
        params.put("response_type", "code");
        params.put("client_id", googleProperties.getClientId());
        params.put("redirect_uri", googleProperties.getCallbackUrl());

        return urlPlusParams(googleProperties.getBaseUrl() + googleProperties.getLoginUri(), params);
    }

    @Override
    public Mono<UserLoginResponse> requestAccessToken(String code, String state) {
        Map<String, Object> params = new HashMap<>();
        params.put("code", code);
        params.put("client_id", googleProperties.getClientId());
        params.put("client_secret", googleProperties.getClientSecret());
        params.put("redirect_uri", googleProperties.getCallbackUrl());
        params.put("grant_type", GRANT_TYPE);

        WebClient webclient =
                WebClient.builder().baseUrl(googleProperties.getTokenUrl())
                        .build()
                ;

        WebClient.ResponseSpec responseSpec = webclient.post()
                .uri(urlPlusParams(googleProperties.getTokenUri(), params))
                .retrieve()
                ;

        UserLoginResponse failResponseBody = new UserLoginResponse();
        return responseSpec.toEntity(GoogleLoginResponse.class)
                .flatMap(response -> {
                    String accessToken = response.getBody().getIdToken();
                    setFailLoginResponse(failResponseBody, accessToken);
                    return getUserInfoByAccessToken(accessToken).toEntity(GoogleProfileResponse.class);
                })
                .map(responseSpecByProperties ->  responseSpecByProperties.getBody().getEmail())
                .flatMap(userService::findByEmail)
                .flatMap(userService::buildUserLoginResponseByUser)
                .switchIfEmpty(Mono.defer(() ->Mono.just(failResponseBody)))
                ;
    }

    @Override
    public Mono<UserLoginResponse> join(UserJoinRequest requestBody) {
        WebClient.ResponseSpec responseSpec = getUserInfoByAccessToken(requestBody.getAccessToken());

        return responseSpec.toEntity(GoogleProfileResponse.class)
                .flatMap(response -> {
                    GoogleProfileResponse body = response.getBody();
                    User user = User.newInstance(
                            body.getEmail(),
                            body.getName(),
                            requestBody.getNickname(),
                            body.getPicture()
                    );
                    return userService.saveUser(user);
                })
                .flatMap(userService::buildUserLoginResponseByUser)
                ;
    }

    @Override
    public WebClient.ResponseSpec getUserInfoByAccessToken(String accessToken) {
        Map<String, Object> params = new HashMap<>();
        String idToken = accessToken;

        if (idToken.startsWith("Bearer ")) {
            idToken = idToken.substring(7);
        }

        params.put("id_token", idToken);

        WebClient webClient =
                WebClient.builder().baseUrl(googleProperties.getProfileUrl())
                        .build();

        return webClient.get()
                .uri(urlPlusParams(googleProperties.getProfileUri(), params))
                .retrieve()
                ;
    }

}
