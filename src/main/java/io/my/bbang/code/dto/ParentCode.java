package io.my.bbang.code.dto;

import lombok.Getter;

@Getter
public enum ParentCode {
    PILGRIMAGE_ADDRESS_CODE("001", "빵지순례 지역 목록"), 
    ;

    private String code;
    private String description;

    private ParentCode(String code, String description) {
        this.code = code;
        this.description = description;
    }
}
