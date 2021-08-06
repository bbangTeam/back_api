package io.my.bbang.pilgrimage.payload.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.my.bbang.commons.payloads.BbangResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter
public class PilgrimageBoardList extends BbangResponse {
    private List<Board> boardList;

    @Getter @Setter
    public static class Board {
        private String content;
        private String nickname;
        private String storeName;
        private LocalDateTime createDate;

        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        private String userId;
    }
}
