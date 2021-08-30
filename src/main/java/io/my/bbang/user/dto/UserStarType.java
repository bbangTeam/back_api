package io.my.bbang.user.dto;

public enum UserStarType {
    STORE("store"),
    PILGRIMAGE("pilgrimage")
    ;

    private final String value;

    UserStarType(String value) {
        this.value = value;
    }

    public String getValue() { return value; }

    public boolean isEqualsType(String type) {
        return this.value.equals(type);
    }

    public static boolean isExistType(String type) {
        for (UserStarType userStarType : UserStarType.values()) {
            if (userStarType.isEqualsType(type)) return true;
        }
        return false;
    }
}
