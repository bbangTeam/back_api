package io.my.bbang.test.vo.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TestLoginResponse {
	private String loginId;
	private String accessToken;
	private String refreshToken;
}