package io.my.bbang.pilgrimage.payload.request;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PilgrimageWriteRequest {
    private String content;
    private String storeId;
    private String storeName;
}
