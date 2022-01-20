package com.highgeupsik.backend.dto;

import java.time.LocalDateTime;

import com.highgeupsik.backend.entity.Comment;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class CommentResDTO {

    private Long id;
    private Long writerId;
    private String content;
    private int anonymousId;
    private int likeCount;
    private boolean isUserLike;
    private boolean isParent;
    private boolean isDeleted;
    private int replyCount;
    private LocalDateTime createdDate;

    public CommentResDTO(Comment comment, boolean isUserLike) {
        this.id = comment.getId();
        this.writerId = comment.getUser().getId();
        this.content = comment.getContent();
        this.anonymousId = comment.getAnonymousId();
        this.likeCount = comment.getLikeCount();
        this.isUserLike = isUserLike;
        this.isParent = comment.isParent();
        this.isDeleted = comment.isDeleteFlag();
        this.replyCount = comment.getReplyCount();
        this.createdDate = comment.getCreatedDate();
    }
}
