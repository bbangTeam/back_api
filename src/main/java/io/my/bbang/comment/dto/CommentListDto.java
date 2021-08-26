package io.my.bbang.comment.dto;

import lombok.Data;

@Data
public class CommentListDto {
	private String nickname;
	private String content;
	private long likeCount;
	private long reCommentCount;
	private long clickCount;
	private boolean isLike;
}
