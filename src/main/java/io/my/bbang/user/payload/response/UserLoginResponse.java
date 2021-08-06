package io.my.bbang.user.payload.response;

import io.my.bbang.commons.payloads.BbangResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserLoginResponse extends BbangResponse {
	private String loginId;
	private String accessToken;
}
