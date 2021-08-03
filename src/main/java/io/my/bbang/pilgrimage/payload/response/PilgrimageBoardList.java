package io.my.bbang.pilgrimage.payload.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class PilgrimageBoardList {
    private List<Board> boardList;

    @Getter @Setter
    public static class Board {
        private String title;
        private String content;
        private String nickname;
        private String storeName;

        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        private String userId;
    }
}
