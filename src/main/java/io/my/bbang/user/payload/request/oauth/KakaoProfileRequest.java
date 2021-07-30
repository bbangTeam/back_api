package io.my.bbang.user.payload.request.oauth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class KakaoProfileRequest {
    @JsonProperty("target_id_type")
    private String targetIdType;

    @JsonProperty("target_id")
    private Long targetId;

    @JsonProperty("secure_resource")
    private Boolean secureResource;

    @JsonProperty("property_keys")
    private String[] propertyKeys;

    public KakaoProfileRequest() {
        this.propertyKeys = new String[]{
                "properties.nickname",
                "properties.profile_image",
                "kakao_account.email"
        };
    }

}
