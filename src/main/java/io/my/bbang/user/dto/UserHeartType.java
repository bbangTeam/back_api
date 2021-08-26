package io.my.bbang.user.dto;

import java.util.HashMap;
import java.util.Map;

public enum UserHeartType {
    STORE("store"),
    BREADSTAGRAM("breadstagram"),
    PILGRIMAGE("pilgrimage"),
    COMMENT("comment"),
    ;
    
    private final String value;

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

    public static UserHeartType getType(String type) {
        return findTypeMap.get(type);
    }

    public boolean isEqualsType(String type) {
        return this.value.equals(type);
    }

    public static boolean isExistType(String type) {
        for (UserHeartType userHeartType : UserHeartType.values()) {
            if (userHeartType.isEqualsType(type)) return true;
        }
        return false;
    }

}
