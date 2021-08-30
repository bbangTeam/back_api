package io.my.bbang.comment.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentListDto {
	private String nickname;
	private String content;
	private long likeCount;
	private long reCommentCount;
	private long clickCount;
	private boolean isLike;
	private LocalDateTime createDate;
	private LocalDateTime modifyDate;
}
