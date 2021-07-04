package io.my.bbang.comment.dto;

import java.util.HashMap;
import java.util.Map;

public enum CommentType {
    BREADSTAGRAM("breadstagram"), 
    RE_COMMENT("reComment"), 
    PILGRIMAGE("pilgrimage"), 
    STORE("store"), 

    ;

    private String value;

    private final static Map<String, CommentType> findTypeMap;
    static {
        findTypeMap = new HashMap<>();
        for(CommentType type : CommentType.values()) {
            findTypeMap.put(type.getValue(), type);
        }
    }

    CommentType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public static CommentType getType(String type) {
        return findTypeMap.get(type);
    }
    
}
