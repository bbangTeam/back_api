package io.my.bbang.comment.dto;

public enum CommentType {
    BREADSTAGRAM("breadstagram"), 
    RE_COMMENT("reComment"), 
    PILGRIMAGE("pilgrimage"), 
    ;

    private String value;

//    private final static Map<String, CommentType> findTypeMap;
//    static {
//        findTypeMap = new HashMap<>();
//        for(CommentType type : CommentType.values()) {
//            findTypeMap.put(type.getValue(), type);
//        }
//    }

    CommentType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean equalsType(String type) {
        return this.value.equals(type);
    }

    public static boolean isExistType(String type) {
        for (CommentType commentType : CommentType.values()) {
            if (commentType.equalsType(type)) return true;
        }
        return false;
    }

}
