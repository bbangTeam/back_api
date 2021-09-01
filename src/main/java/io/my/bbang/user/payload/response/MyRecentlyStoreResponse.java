package io.my.bbang.user.payload.response;

import io.my.bbang.commons.payloads.BbangResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter
public class MyRecentlyStoreResponse extends BbangResponse {
    private List<RecentlyStore> list;

    @Getter @Setter
    public static class RecentlyStore {
        private String id;
        private String storeName;
        private String imageUrl;
        private double distance;
        private LocalDateTime clickDate;
        private String fullNm;
        private String sigKorNm;
    }

}
