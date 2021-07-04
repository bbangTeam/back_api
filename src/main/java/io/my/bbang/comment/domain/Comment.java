package io.my.bbang.comment.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import io.my.bbang.comment.dto.CommentType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Document("comment")
public class Comment {
    
    @Id
    private String id;
    private String parentId;

    private String nickname;
    private String content;

    private String type;

    private LocalDateTime createDate;
    private LocalDateTime modifyDate;

    public void setType(CommentType type) {
        setType(type.getValue());
    }

    public void setType(String type) {
        this.type = type;
    }

    public CommentType getType() {
        return CommentType.getType(type);
    }

    public static Comment build(String parentId, String nickname, String content, CommentType type) {
        return build(parentId, nickname, content, type.getValue());
    }

    public static Comment build(String parentId, String nickname, String content, String type) {
        Comment comment = new Comment();
        comment.setParentId(parentId);
        comment.setCreateDate(LocalDateTime.now());
        comment.setModifyDate(LocalDateTime.now());
        comment.setContent(content);
        comment.setNickname(nickname);
        comment.setType(type);
        return comment;
    }

}
