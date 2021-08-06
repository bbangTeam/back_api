package io.my.bbang.comment.payload.response;

import io.my.bbang.commons.payloads.BbangResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CommentCountResponse extends BbangResponse {
    private Integer count;
    
}
