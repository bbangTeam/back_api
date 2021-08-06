package io.my.bbang.commons.exception;

import io.my.bbang.commons.exception.type.ExceptionTypes;
import lombok.Getter;

@Getter
public class BbangException extends RuntimeException {
	private static final long serialVersionUID = 8888926686884785342L;
	private final ExceptionTypes type;
	
	public BbangException(ExceptionTypes type) {
		this.type = type;
	}
}
