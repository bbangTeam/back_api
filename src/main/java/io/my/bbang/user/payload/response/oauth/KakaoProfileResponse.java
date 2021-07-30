package io.my.bbang.user.payload.response.oauth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class KakaoProfileResponse {
    private Long id;
    private Boolean hasSignedUp;

    @JsonProperty("connected_at")
    private LocalDateTime connetedAt;
    @JsonProperty("synched_at")
    private LocalDateTime synchedAt;


    private Properties properties;
    @JsonProperty("kakao_account")
    private KakaoAcount kakaoAcount;

    @Getter @Setter
    public static class Properties {
        private String nickname;
        @JsonProperty("profile_image")
        private String profileImage;
        @JsonProperty("thumbnail_image")
        private String thumbnailImage;
    }

    @Getter @Setter
    public static class KakaoAcount {
        @JsonProperty("profile_needs_agreement")
        private Boolean profileNeedsAgreement;

        @JsonProperty("profile_nickname_needs_agreement")
        private Boolean profileNicknameNeedsAgreement;

        @JsonProperty("profile_image_needs_agreement")
        private Boolean profileImageNeedsAgreement;

        @JsonProperty("email_needs_agreement")
        private Boolean emailNeedsAgreement;

        @JsonProperty("is_email_valid")
        private Boolean isEmailValid;

        @JsonProperty("is_email_verified")
        private Boolean isEmailVerified;

        @JsonProperty("age_range_needs_agreement")
        private Boolean ageRangeNeedsAgreement;

        @JsonProperty("birthyear_needs_agreement")
        private Boolean birthyearNeedsAgreement;

        @JsonProperty("birthday_needs_agreement")
        private Boolean birthdayNeedsAgreement;

        @JsonProperty("gender_needs_agreement")
        private Boolean genderNeedsAgreement;

        @JsonProperty("phone_number_needs_agreement")
        private Boolean phoneNumberNeedsAgreement;

        @JsonProperty("ci_needs_agreement")
        private Boolean ciNeedsAgreement;

        @JsonProperty("has_email")
        private Boolean hasEmail;

        private String email;

        @JsonProperty("age_range")
        private String ageRange;

        private String birthyear;

        private String birthday;

        @JsonProperty("birthday_type")
        private String birthdayType;

        private String gender;

        @JsonProperty("phone_number")
        private String phoneNumber;

        private String ci;

        @JsonProperty("ci_authenticated_at")
        private LocalDateTime ciAuthenticatedAt;

        private Profile profile;

        @Getter @Setter
        public static class Profile {
            private String nickname;

            @JsonProperty("thumbnail_image_url")
            private String thumbnailImageUrl;

            @JsonProperty("profile_image_url")
            private String profileImageUrl;

            @JsonProperty("is_default_image")
            private Boolean isDefaultImage;
        }
    }
}
