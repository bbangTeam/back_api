package io.my.bbang.user.service.oauth;

import io.my.bbang.commons.properties.OauthProperties;
import io.my.bbang.user.payload.response.oauth.KakaoLoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class KakaoOauthService implements SocialOauthService {
    private final OauthProperties.Kakao kakaoProperties;

    private static final String GRANT_TYPE = "authorization_code";

    @Override
    public String getOauthRedirectURL() {
        Map<String, Object> params = new HashMap<>();

        params.put("client_id", kakaoProperties.getClientId());
        params.put("redirect_uri", kakaoProperties.getCallbackUrl());
        params.put("response_type", "code");

        return urlPlusParams(kakaoProperties.getBaseUrl() + kakaoProperties.getLoginUri(), params);
    }

    @Override
    public Mono<String> requestAccessToken(String code, String state) {

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

        return responseSpec.toEntity(KakaoLoginResponse.class)
                .map(response -> {
                    return response.getBody().getAccessToken();
                });
    }
}
