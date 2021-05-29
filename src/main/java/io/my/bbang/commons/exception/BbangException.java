package io.my.bbang.commons.exception;

import lombok.Getter;

@Getter
public class BbangException extends RuntimeException {
	private static final long serialVersionUID = 8888926686884785342L;
	private String message;
	
	public BbangException(String message) {
		this.message = message;
	}
}
