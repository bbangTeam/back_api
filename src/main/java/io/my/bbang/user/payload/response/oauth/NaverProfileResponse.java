package io.my.bbang.user.payload.response.oauth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class NaverProfileResponse {
    private String resultCode;
    private String message;
    private ProfileResponse response;

    @Getter @Setter
    public static class ProfileResponse {
        private String id;
        private String nickname;
        private String name;
        private String email;
        private String gender;
        private String age;
        private String birthday;

        @JsonProperty("profile_image")
        private String profileImage;
        private String birthyear;
        private String mobile;

    }
}
