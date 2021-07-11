package io.my.bbang.commons.exception.advice;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.function.server.EntityResponse;

import io.my.bbang.commons.exception.BbangException;
import io.my.bbang.commons.exception.type.ExceptionTypes;
import io.my.bbang.commons.payloads.BbangResponse;
import reactor.core.publisher.Mono;

@RestControllerAdvice
public class ExceptionAdvice {
	
    @ExceptionHandler(BbangException.class)
    protected Mono<EntityResponse<BbangResponse>> handleHttpRequestMethodNotSupportedException(BbangException e) {
        ExceptionTypes exceptionType = e.getType();

        return EntityResponse.fromObject(
            new BbangResponse(e.getMessage())).status(exceptionType.getStatus()).build();
    }


    @ExceptionHandler(Exception.class)
    protected Mono<EntityResponse<BbangResponse>> exceptionAdvice(Exception e) {
        e.printStackTrace();
        return EntityResponse.fromObject(
            new BbangResponse("서버 에러")).status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
	
}
