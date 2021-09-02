package io.my.bbang.pilgrimage.payload.response;

import io.my.bbang.commons.payloads.BbangResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter
public class PilgrimageBoardListResponse extends BbangResponse {
    private List<Board> boardList;

    @Getter @Setter
    public static class Board {
        private String id;
        private String title;
        private String content;

        private String storeId;
        private String storeName;
        private String nickname;

        private long commentCount;
        private long clickCount;

        private LocalDateTime createDate;
        private LocalDateTime modifyDate;
    }
}
