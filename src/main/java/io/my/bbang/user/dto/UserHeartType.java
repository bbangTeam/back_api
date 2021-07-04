package io.my.bbang.user.dto;

import java.util.HashMap;
import java.util.Map;

public enum UserHeartType {
    STORE("store"), 
    ;
    
    private String value;

    private final static Map<String, UserHeartType> findTypeMap;
    static {
        findTypeMap = new HashMap<>();
        for(UserHeartType type : UserHeartType.values()) {
            findTypeMap.put(type.getValue(), type);
        }
    }

    UserHeartType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public static UserHeartType getType(String type) {
        return findTypeMap.get(type);
    }
    
}
