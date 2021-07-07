package io.my.bbang.pilgrimage.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Document("pilgrimage_address")
public class PilgrimageAddress {

    @Id
    private String id;
    private String cityName;
    
}
