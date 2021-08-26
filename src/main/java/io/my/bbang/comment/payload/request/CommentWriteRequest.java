package io.my.bbang.comment.payload.request;

import lombok.Data;

@Data
public class CommentWriteRequest {
	private String id;
	private String content;
	private String type;
	private String parentId;
}
