package io.my.bbang.user.payload.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MyProfileResponse {
    private Long postCount;
    private Long commentCount;
    private Long likeCount;
    private String nickname;
    private String email;
    private String profileImageUrl;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String userId;

}
