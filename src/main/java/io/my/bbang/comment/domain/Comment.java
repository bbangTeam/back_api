package io.my.bbang.comment.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import io.my.bbang.comment.dto.CommentType;
import io.my.bbang.commons.entity.BaseTimeEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Document("comment")
public class Comment  extends BaseTimeEntity {
    
    @Id
    private String id;
    private String parentId;
    private String userId;
    private String content;

    private long likeCount;
    private long clickCount;
    private long reCommentCount;

    private String type;

    public void setType(CommentType type) {
        setType(type.getValue());
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public static Comment build(String parentId, String userId, String content, CommentType type) {
        return build(parentId, userId, content, type.getValue());
    }

    public static Comment build(String parentId, String userId, String content, String type) {
        Comment comment = new Comment();
        comment.setParentId(parentId);
        comment.setUserId(userId);
        comment.setContent(content);
        comment.setType(type);

        return comment;
    }

}
