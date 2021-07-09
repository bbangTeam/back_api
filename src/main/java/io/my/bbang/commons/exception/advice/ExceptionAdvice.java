package io.my.bbang.commons.exception.advice;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.function.server.EntityResponse;

import io.my.bbang.commons.exception.BbangException;
import io.my.bbang.commons.payloads.BbangResponse;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@RestControllerAdvice
public class ExceptionAdvice {
	
    @ExceptionHandler(BbangException.class)
    protected Mono<EntityResponse<BbangResponse>> handleHttpRequestMethodNotSupportedException(BbangException e) {
        
        log.info("====================================");
        log.info("exception advice!!!");
        log.info("====================================");
        
        return EntityResponse.fromObject(
            new BbangResponse("BbangException")).status(HttpStatus.BAD_REQUEST).build();
    }


    @ExceptionHandler(Exception.class)
    protected Mono<EntityResponse<BbangResponse>> exceptionAdvice(Exception e) {
        
        log.info("====================================");
        log.info("exception advice!!!");
        log.info("====================================");
        
        return EntityResponse.fromObject(new BbangResponse("Exception")).status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
	
}
