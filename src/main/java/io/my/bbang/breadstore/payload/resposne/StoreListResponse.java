package io.my.bbang.breadstore.payload.resposne;

import io.my.bbang.commons.payloads.BbangResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class StoreListResponse extends BbangResponse {
    List<StoreList> storeList;

    @Getter @Setter
    public static class StoreList {
        private String id;          // id
        private String storeName;   // entrpNm
        private double longitude;   // xposLo
        private double latitude;    // yposLa
        private String imageUrl;    // naverThumbUrl;
        private String openHour;    // businessHours
        private String tel;         // telNo
        private String loadAddr;    // loadAddr
        private double star;
        private long likeCount;
        private long reviewCount;
    }
}
