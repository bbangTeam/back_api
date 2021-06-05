package io.my.bbang.user.payload.response;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class UserJoinResponse {
	private String id;
	private String loginId;
	private LocalDateTime createTime;
}
