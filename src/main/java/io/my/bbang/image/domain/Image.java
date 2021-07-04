package io.my.bbang.image.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import io.my.bbang.commons.entity.BaseTimeEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Document("image")
public class Image extends BaseTimeEntity {
    @Id
    private String id;
    private String url;
    private String fileName;
    private Integer num;

    public static Image build(String url, String fileName) {
        Image image = new Image();
        image.setUrl(url);
        image.setFileName(fileName);
        return image;
    }
}
