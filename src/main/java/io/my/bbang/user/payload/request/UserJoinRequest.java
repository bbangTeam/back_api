package io.my.bbang.user.payload.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class UserJoinRequest {
	@NotNull
	private String nickname;

	@NotNull
	private String accessToken;

	public String getAccessToken() {
		return "Bearer " + this.accessToken;
	}


}
