package io.my.bbang.user.service.oauth;

import io.my.bbang.user.dto.SocialLoginType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OauthService {
    private final List<SocialOauthService> socialOauthList;

    public Mono<String> requestAccessToken(SocialLoginType socialLoginType, String code, String state) {
        SocialOauthService socialOauthService = findSocialOauthByType(socialLoginType);
        return socialOauthService.requestAccessToken(code, state);
    }

    public SocialOauthService findSocialOauthByType(SocialLoginType socialLoginType) {
        return socialOauthList.stream()
                .filter(x -> x.type() == socialLoginType)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("알 수 없는 SocialLoginType 입니다."));
    }
}