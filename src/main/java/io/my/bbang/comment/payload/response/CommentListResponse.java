package io.my.bbang.comment.payload.response;

import java.util.ArrayList;
import java.util.List;

import io.my.bbang.comment.dto.CommentListDto;
import io.my.bbang.commons.payloads.BbangResponse;
import lombok.Data;

@Data
public class CommentListResponse extends BbangResponse {
	private List<CommentListDto> commentList;
	
	public CommentListResponse() {
		commentList = new ArrayList<>();
	}
}
