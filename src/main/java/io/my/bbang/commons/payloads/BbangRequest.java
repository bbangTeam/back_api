package io.my.bbang.commons.payloads;

import lombok.Data;

@Data
public class BbangRequest {
	private String loginId;
	private String password;
}