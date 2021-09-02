package io.my.bbang.breadstore.payload.resposne;

import io.my.bbang.commons.payloads.BbangResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter
public class StoreBreadListResponse extends BbangResponse {
    private List<BreadList> breadList;

    @Getter @Setter
    public static class BreadList {
        private String id;
        private String name;
        private int price;
        private LocalDateTime modifyDate;
    }
}
