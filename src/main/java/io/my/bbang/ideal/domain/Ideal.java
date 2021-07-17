package io.my.bbang.ideal.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Document("ideal")
public class Ideal {

    @Id
    private String id;

    private String breadName;
    private String imageUrl;

    private Long selectedCount;
}
