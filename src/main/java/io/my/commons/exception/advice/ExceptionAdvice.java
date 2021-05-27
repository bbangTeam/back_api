package io.my.commons.exception.advice;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import io.my.commons.exception.BbangException;
import io.my.commons.payloads.BbangResponse;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@ControllerAdvice
public class ExceptionAdvice {
	
    @ExceptionHandler(BbangException.class)
    protected Mono<BbangResponse> handleHttpRequestMethodNotSupportedException(BbangException e) {
        log.error("BbangException", e);
        return Mono.just(new BbangResponse("BbangException"));
    }
	
}
