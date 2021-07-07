package io.my.bbang.code.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Document("code")
@Getter @Setter
public class Code {
    @Id
    private String id;
    private String code;
    private String parentCode;
    private String content;
    private String description;
}
