package io.my.bbang.commons.exception.advice;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import io.my.bbang.commons.exception.BbangException;
import io.my.bbang.commons.payloads.BbangResponse;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@RestControllerAdvice
public class ExceptionAdvice {
	
    @ExceptionHandler(BbangException.class)
    protected Mono<BbangResponse> handleHttpRequestMethodNotSupportedException(BbangException e) {
        
    	log.error("BbangException", e);
        
        return Mono.just(new BbangResponse("BbangException"));
    }
	
}
