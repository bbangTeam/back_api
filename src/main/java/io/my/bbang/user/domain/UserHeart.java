package io.my.bbang.user.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import io.my.bbang.user.dto.UserHeartType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@Document("userHeart")
public class UserHeart {

    @Id
    private String id;
    private String userId;
    private String parentId;
    private String type;

    public void setType(UserHeartType type) {
        setType(type.getValue());
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public static UserHeart build(String userId, String heartId, UserHeartType type) {
        return build(userId, heartId, type.getValue());
    }

    public static UserHeart build(String userId, String parentId, String type) {
        UserHeart userHeart = new UserHeart();
        userHeart.setParentId(parentId);
        userHeart.setType(type);
        userHeart.setUserId(userId);
        return userHeart;
    }


    
}
