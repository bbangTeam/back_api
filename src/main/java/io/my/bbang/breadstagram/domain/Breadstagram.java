package io.my.bbang.breadstagram.domain;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import io.my.bbang.breadstagram.dto.BreadstagramImageDto;
import io.my.bbang.commons.entity.BaseTimeEntity;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Document("breadstagram")
public class Breadstagram extends BaseTimeEntity {

    @Id
    private String id;

    private String storeId;
    private String cityName;
    private String breadName;
    private String storeName;
    private List<BreadstagramImageDto> imageList;

}
