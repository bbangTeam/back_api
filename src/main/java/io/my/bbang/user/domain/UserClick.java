package io.my.bbang.user.domain;

import io.my.bbang.commons.entity.BaseTimeEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Setter
@Getter
@Document("userClick")
public class UserClick extends BaseTimeEntity {

    @Id
    private String id;
    private String userId;
    private String parentId;
    private String type;
}
