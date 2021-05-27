package io.my.bbang.test.vo.response;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TestJoinResponse {
	
	private String id;
	private String loginId;
	private LocalDateTime createTime;

}
