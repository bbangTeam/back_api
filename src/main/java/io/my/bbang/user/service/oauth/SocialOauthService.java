package io.my.bbang.user.service.oauth;

import io.my.bbang.user.dto.SocialLoginType;
import io.my.bbang.user.payload.request.UserJoinRequest;
import io.my.bbang.user.payload.response.UserLoginResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.stream.Collectors;

public interface SocialOauthService {
    String getOauthRedirectURL();
    Mono<UserLoginResponse> requestAccessToken(String code, String state);
    Mono<UserLoginResponse> join(UserJoinRequest requestBody);
    WebClient.ResponseSpec getUserInfoByAccessToken(String accessToken);

    default SocialLoginType type() {
        if (this instanceof GoogleOauthService) {
            return SocialLoginType.GOOGLE;
        } else if (this instanceof KakaoOauthService) {
            return SocialLoginType.KAKAO;
        } else if (this instanceof NaverOauthService) {
            return SocialLoginType.NAVER;
        }

        return null;
    }

    default String urlPlusParams(String url, Map<String, Object> params) {
        String parameterString = params.entrySet().stream()
                .map(x-> x.getKey() + "=" + x.getValue())
                .collect(Collectors.joining("&"));

        return url + "?" + parameterString;
    }

    default void setFailLoginResponse(UserLoginResponse failResponseBody, String accessToken) {
        failResponseBody.setAccessToken(accessToken);
        failResponseBody.setCode(1);
        failResponseBody.setResult("No Join User");
    }

}
