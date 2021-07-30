package io.my.bbang.commons.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

public class OauthProperties {

    @Getter @Setter @Component
    @ConfigurationProperties("oauth.google")
    public static class Google {
        private String clientId;
        private String clientSecret;
        private String baseUrl;
        private String tokenUrl;
        private String callbackUrl;
        private String loginUri;
        private String tokenUri;
        private String profileUrl;
        private String profileUri;
    }

    @Getter @Setter @Component
    @ConfigurationProperties("oauth.naver")
    public static class Naver {
        private String clientId;
        private String clientSecret;
        private String baseUrl;
        private String callbackUrl;
        private String loginUri;
        private String tokenUri;
        private String profileUrl;
        private String profileUri;
    }

    @Getter @Setter @Component
    @ConfigurationProperties("oauth.kakao")
    public static class Kakao {
        private String clientId;
        private String clientSecret;
        private String baseUrl;
        private String callbackUrl;
        private String loginUri;
        private String tokenUri;
        private String profileUrl;
        private String profileUri;
    }

}
