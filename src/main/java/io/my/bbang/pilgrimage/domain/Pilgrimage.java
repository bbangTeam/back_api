package io.my.bbang.pilgrimage.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Document("pilgrimage")
public class Pilgrimage {

    @Id
    private String id;
    private String storeId;
    private String breadName;

    // common_code의 parent_code = 001 인 항목 참고
    private String pilgrimageAddressId;
    private String detailAddress;

    private String fileName;
    private String imageUrl;
    
}
