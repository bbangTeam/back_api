package io.my.bbang.comment.payload.response;

import io.my.bbang.commons.payloads.BbangResponse;
import lombok.Data;

@Data
public class CommentWriteResponse extends BbangResponse {
	private String id;
}
