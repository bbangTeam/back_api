package io.my.bbang.comment.payload.request;

import lombok.Data;

@Data
public class CommentListRequest {
	private String id;
	private String type;
	private Integer pageSize;
	private Integer pageNum;

}
