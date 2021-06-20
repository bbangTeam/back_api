package io.my.bbang.user.payload.response;

import lombok.Data;

@Data
public class UserLoginResponse {
	private String loginId;
	private String accessToken;
}
