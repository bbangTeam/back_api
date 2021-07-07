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
    private String pilgrimageAddressId;
    private String detailAddress;

    private String fileName;
    private String imageUrl;
    
}
