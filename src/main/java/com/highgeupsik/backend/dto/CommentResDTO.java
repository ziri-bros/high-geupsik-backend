package com.highgeupsik.backend.dto;

import com.highgeupsik.backend.entity.Comment;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentResDTO {

	private Long id;
	private Long writerId;
	private String content;
	private int userCount;
	private int likeCount;
	private boolean isUserLike;
	private boolean isParent;

	public CommentResDTO(Comment comment, boolean isUserLike) {
		this.id = comment.getId();
		this.writerId = comment.getUser().getId();
		this.content = comment.getContent();
		this.userCount = comment.getUserCount();
		this.likeCount = comment.getLikeCount();
		this.isUserLike = isUserLike;
		this.isParent = comment.isParent();
	}

}
