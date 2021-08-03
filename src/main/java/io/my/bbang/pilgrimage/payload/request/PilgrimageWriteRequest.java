package io.my.bbang.pilgrimage.payload.request;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PilgrimageWriteRequest {
    private String title;
    private String content;
    private String storeId;
    private String storeName;
}
