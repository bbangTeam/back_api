package io.my.bbang.comment.payload.response;

import java.util.ArrayList;
import java.util.List;

import io.my.bbang.comment.dto.CommentListDto;
import lombok.Data;

@Data
public class CommentListResponse {
	private String result;
	private List<CommentListDto> commentList;
	
	public CommentListResponse() {
		commentList = new ArrayList<>();
	}
}
