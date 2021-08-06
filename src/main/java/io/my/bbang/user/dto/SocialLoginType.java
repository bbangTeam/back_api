package io.my.bbang.user.dto;

public enum SocialLoginType {
    GOOGLE("google"),
    NAVER("naver"),
    KAKAO("kakao"),
    ;

    private final String name;

    SocialLoginType(String name) {
        this.name = name;
    }

    public static SocialLoginType findByName(String name) {
        for (SocialLoginType value : SocialLoginType.values()) {
            if (value.name.equals(name)) {
                return value;
            }
        }

        throw new RuntimeException("socialLogin name is wrong");
    }

    public String getName() {
        return this.name;
    }

}
