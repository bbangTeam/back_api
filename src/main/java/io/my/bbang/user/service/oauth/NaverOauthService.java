package io.my.bbang.user.service.oauth;

import io.my.bbang.commons.properties.OauthProperties;
import io.my.bbang.user.payload.response.oauth.NaverLoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class NaverOauthService implements SocialOauthService {
    private final OauthProperties.Naver naverProperties;

    private static final String GRANT_TYPE = "authorization_code";

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
    public Mono<String> requestAccessToken(String code, String state) {

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


        return responseSpec.toEntity(NaverLoginResponse.class)
                .map(response -> {
                    return response.getBody().getAccessToken();
                });
    }
}
