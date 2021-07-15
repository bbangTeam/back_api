package io.my.bbang.commons.payloads;

import lombok.Data;

@Data
public class BbangResponse {
	private String result;
	private Integer code;

	public BbangResponse() {
		this("Success");
	}

	public BbangResponse(String result) {
		this(0, result);
	}

	public BbangResponse(Integer code, String result) {
		this.code = code;
		this.result = result;
	}


}
