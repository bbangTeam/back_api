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
    
    // like_id => 현재는 store의 ID만 존재함.
    private String heartId;

    // 현재는 빵스타그램에만 좋아요 존재. - 빵스타그램의 좋아요는 store로 들어감.
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

    public static UserHeart build(String userId, String heartId, String type) {
        UserHeart userHeart = new UserHeart();
        userHeart.setHeartId(heartId);
        userHeart.setType(type);
        userHeart.setUserId(userId);
        return userHeart;
    }


    
}
