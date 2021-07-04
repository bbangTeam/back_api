package io.my.bbang.breadstagram.domain;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import io.my.bbang.breadstagram.dto.BreadstagramImageDto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Document("breadstagram")
public class Breadstagram {

    @Id
    private String id;

    private String storeId;
    private String cityName;
    private String breadName;
    private List<BreadstagramImageDto> imageList;

    private LocalDateTime createDate;
    
}
