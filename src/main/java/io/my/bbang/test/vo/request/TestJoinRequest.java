package io.my.bbang.test.vo.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class TestJoinRequest {
	
	@NotNull
	private String name;
	
	@NotNull
	@Size(min = 6, max = 20)
	private String loginId;
	
	@NotNull
	@Size(min = 8, max = 20)
	private String password;
}
