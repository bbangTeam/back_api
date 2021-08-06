package io.my.bbang.user.payload.response;

import java.time.LocalDateTime;

import io.my.bbang.commons.payloads.BbangResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserJoinResponse extends BbangResponse {
	private String id;
	private String loginId;
	private LocalDateTime createTime;
}
