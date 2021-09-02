package io.my.bbang.breadstore.payload.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class StoreMenuPatchRequest {
    private String id;
    private String storeId;
    private String name;
    private int price;

    private List<String> bakeTimeList;
}
