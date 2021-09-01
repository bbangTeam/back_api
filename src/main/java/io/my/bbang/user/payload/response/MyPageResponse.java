package io.my.bbang.user.payload.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.my.bbang.commons.payloads.BbangResponse;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MyPageResponse extends BbangResponse {
    private long postCount;
    private long commentCount;
    private long likeCount;
    private String nickname;
    private String email;
    private String profileImageUrl;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String userId;

}
