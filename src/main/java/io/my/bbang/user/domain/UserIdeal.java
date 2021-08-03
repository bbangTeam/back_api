package io.my.bbang.user.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Document("userIdeal")
public class UserIdeal {

    @Id
    private String id;

    private String userId;
    private String idealId;
}
