package com.highgeupsik.backend.api.comment;

import java.time.LocalDateTime;

import com.highgeupsik.backend.entity.board.Comment;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class CommentResDTO {

    private Long id;
    private Long writerId;
    private Long boardId;
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
        this.boardId = comment.getBoard().getId();
        this.content = comment.getContent();
        this.anonymousId = comment.getAnonymousId();
        this.likeCount = comment.getLikeCount();
        this.isUserLike = isUserLike;
        this.isParent = comment.isParent();
        this.isDeleted = comment.isDeleteFlag();
        this.replyCount = comment.getReplyCount();
        this.createdDate = comment.getCreatedDate();
    }

    public CommentResDTO(Comment comment) {
        this.id = comment.getId();
        this.writerId = comment.getUser().getId();
        this.boardId = comment.getBoard().getId();
        this.content = comment.getContent();
        this.anonymousId = comment.getAnonymousId();
        this.likeCount = comment.getLikeCount();
        this.isParent = comment.isParent();
        this.isDeleted = comment.isDeleteFlag();
        this.replyCount = comment.getReplyCount();
        this.createdDate = comment.getCreatedDate();
    }
}
