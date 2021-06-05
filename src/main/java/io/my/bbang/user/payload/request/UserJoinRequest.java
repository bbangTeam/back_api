package io.my.bbang.user.payload.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class UserJoinRequest {
	@NotNull
	private String name;
	
	@NotNull
	@Size(min = 6, max = 20)
	private String loginId;
	
	@NotNull
	@Size(min = 8, max = 20)
	private String password;
}
