package io.my.bbang.user.service.oauth;

import io.my.bbang.commons.properties.OauthProperties;
import io.my.bbang.user.payload.response.oauth.GoogleLoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GoogleOauthService implements  SocialOauthService {
    private final OauthProperties.Google googleProperties;

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
    public Mono<String> requestAccessToken(String code, String state) {
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

        return responseSpec.toEntity(GoogleLoginResponse.class)
                .map(response -> {
                    return response.getBody().getAccessToken();
                })
                ;
    }

}
