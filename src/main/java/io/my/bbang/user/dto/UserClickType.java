package io.my.bbang.user.dto;

public enum UserClickType {
    STORE("store"),
    BREADSTAGRAM("breadstagram"),
    ;
    private final String value;

    UserClickType(String value) {
        this.value = value;
    }
    public String getValue() { return value; }

    public boolean isEqualsType(String type) {
        return this.value.equals(type);
    }

    public static boolean isExistType(String type) {
        for (UserClickType userClickType : UserClickType.values()) {
            if (userClickType.isEqualsType(type)) return true;
        }
        return false;
    }
}
