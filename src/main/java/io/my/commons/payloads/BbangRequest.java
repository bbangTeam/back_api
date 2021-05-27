package io.my.commons.payloads;

import lombok.Data;

@Data
public class BbangRequest {
	private String loginId;
	private String password;
}