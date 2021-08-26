package io.my.bbang.commons.exception.type;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum ExceptionTypes {
    AUTH_EXCEPTION(HttpStatus.UNAUTHORIZED, 1, "인증 에러"),
    DATABASE_EXCEPTION(HttpStatus.BAD_REQUEST, 2, "DB 에러"),
    TYPE_EXCEPTION(HttpStatus.BAD_REQUEST, 3, "타입 에러"),
    ;

    HttpStatus status;
    String message;
    int returnCode;

    ExceptionTypes(HttpStatus status, int returnCode, String message){
        this.status = status;
        this.returnCode = returnCode;
        this.message = message;
    }
    
}
