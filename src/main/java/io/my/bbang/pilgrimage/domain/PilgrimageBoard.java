package io.my.bbang.pilgrimage.domain;

import io.my.bbang.commons.entity.BaseTimeEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter @Setter
@Document("pilgrimageBoard")
public class PilgrimageBoard extends BaseTimeEntity {
    @Id
    private String id;
    private String storeId;
    private String userId;
    private String content;
    private String storeName;
}
