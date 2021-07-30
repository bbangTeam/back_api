package io.my.bbang.user.payload.response.oauth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class GoogleProfileResponse {
    private String iss;
    private String azp;
    private String aud;
    private String sub;
    private String email;
    @JsonProperty("email_verified")
    private Boolean emailVerified;
    @JsonProperty("at_hash")
    private String atHash;
    private String name;
    private String picture;
    @JsonProperty("given_name")
    private String givenName;
    @JsonProperty("family_name")
    private String familyName;
    private String locale;
    private Long iat;
    private Long exp;
    private String alg;
    private String kid;
    private String typ;

}
