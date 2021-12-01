package com.highgeupsik.backend.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.highgeupsik.backend.dto.CommentReqDTO;
import com.highgeupsik.backend.exception.NotMatchException;
import com.highgeupsik.backend.utils.ErrorMessage;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
public class Comment extends TimeEntity {

	@Id
	@GeneratedValue
	@Column(name = "comment_id")
	private Long id;

	private String content;

	private int anonymousId;

	private int replyCount = 1;

	private int likeCount = 0;

	private boolean deleteFlag = false;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "board_id")
	private Board board;

	@OneToMany(mappedBy = "comment")
	private List<Like> likeList = new ArrayList<>();

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_id")
	private Comment parent;

	@OneToMany(mappedBy = "parent")
	private List<Comment> children = new ArrayList<>();

	@Builder
	public Comment(String content, int anonymousId, User user, Board board) {
		this.content = content;
		this.anonymousId = anonymousId;
		this.user = user;
		this.board = board;
	}

	public static Comment of(String content, User user, Board board) {
		Comment comment = Comment.builder()
			.content(content)
			.user(user)
			.board(board)
			.build();
		comment.parent = comment;
		comment.children.add(comment);
		return comment;
	}

	public int getReplyCount() {
		return children.size() - 1;
	}

	public void setBoard(Board board) {
		this.board = board;
		if (!board.getCommentList().contains(this)) {
			board.getCommentList().add(this);
		}
	}

	public void setAnonymousId(int anonymousNumber) {
		this.anonymousId = anonymousNumber;
	}

	public void checkWriter(Long userId) {
		if (!isWriter(userId)) {
			throw new NotMatchException(ErrorMessage.WRITER_NOT_MATCH);
		}
	}

	private boolean isWriter(Long userId) {
		return user.getId().equals(userId);
	}

	public boolean isParent() {
		return this.equals(parent);
	}

	public boolean isReply() {
		return !isParent();
	}

	public void toReply(Comment parent) {
		this.parent = parent;
		parent.addReply(this);
	}

	private void addReply(Comment comment) {
		children.add(comment);
		replyCount++;
	}

	public void deleteReply(Comment comment) {
		children.remove(comment);
		replyCount--;
	}

	public boolean isDisabled() {
		return deleteFlag;
	}

	public void disable() {
		deleteFlag = true;
	}

	public boolean canDelete() {
		return replyCount == 1;
	}

	public void deleteIfCan() {
		if (isDisabled() && canDelete()) {
			board.deleteComment(this);
		}
	}

	public void updateContent(CommentReqDTO commentReqDTO) {
		content = commentReqDTO.getContent();
	}

	public void updateCommentLike(Boolean flag) {
		if (flag) {
			this.likeCount++;
		} else if (!flag && likeCount > 0) {
			this.likeCount--;
		}
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || !o.getClass().equals(Comment.class)) {
			return false;
		}
		Comment other = (Comment)o;
		return id.equals(other.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}
