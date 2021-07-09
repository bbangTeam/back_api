package io.my.bbang.commons.exception.type;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum ExceptionTypes {
    AUTH_EXCEPTION(HttpStatus.UNAUTHORIZED, 1, "인증 에러"), 
    EMPTY_EXCEPTION(HttpStatus.BAD_REQUEST, 2, "DB 에러"),
    ;

    HttpStatus status;
    String message;
    int returnCode;

    ExceptionTypes(HttpStatus status, int returnCode, String message){
        
    }
    
}
