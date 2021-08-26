package io.my.bbang.user.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Setter
@Getter
@NoArgsConstructor
@Document("userStar")
public class UserStar {
    @Id
    private String id;
    private String userId;
    private String parentId;
    private int star;
    private String type;
}
