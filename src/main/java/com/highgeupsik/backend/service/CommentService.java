package com.highgeupsik.backend.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.highgeupsik.backend.dto.CommentReqDTO;
import com.highgeupsik.backend.dto.CommentResDTO;
import com.highgeupsik.backend.entity.Board;
import com.highgeupsik.backend.entity.Comment;
import com.highgeupsik.backend.entity.User;
import com.highgeupsik.backend.exception.NotFoundException;
import com.highgeupsik.backend.exception.NotMatchException;
import com.highgeupsik.backend.repository.BoardRepository;
import com.highgeupsik.backend.repository.CommentRepository;
import com.highgeupsik.backend.repository.UserRepository;
import com.highgeupsik.backend.utils.ErrorMessage;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

	private final CommentRepository commentRepository;
	private final BoardRepository boardRepository;
	private final UserRepository userRepository;

	public CommentResDTO saveComment(Long userId, Long boardId, CommentReqDTO dto) {
		User writer = userRepository.findById(userId)
			.orElseThrow(() -> new NotFoundException(ErrorMessage.USER_NOT_FOUND));
		Board board = boardRepository.findById(boardId)
			.orElseThrow(() -> new NotFoundException(ErrorMessage.BOARD_NOT_FOUND));

		Comment comment = commentRepository.save(Comment.of(dto.getContent(), writer, board));

		comment.setAnonymousId(getAnonymousNumberFrom(board, writer));
		if (board.isWriter(writer)) {
			comment.setAnonymousId(-1);
		}

		if (dto.getParentId() != null) {
			transformToReply(comment, dto.getParentId());
		}

		return new CommentResDTO(comment, false);
	}

	private int getAnonymousNumberFrom(Board board, User writer) {
		return commentRepository.findFirstByBoardAndUser(board, writer)
			.map(Comment::getAnonymousId)
			.orElse(board.getNextAnonymousNumber());
	}

	private void transformToReply(Comment comment, Long parentId) {
		Comment parent = commentRepository.findById(parentId)
			.orElseThrow(() -> new NotFoundException(ErrorMessage.COMMENT_NOT_FOUND));
		comment.toReply(parent);
	}

	public Long updateComment(Long userId, Long commentId, CommentReqDTO commentReqDTO) {
		Comment comment = commentRepository.findById(commentId)
			.orElseThrow(() -> new NotFoundException(ErrorMessage.COMMENT_NOT_FOUND));
		checkWriter(comment, userId);
		comment.updateContent(commentReqDTO);
		return commentId;
	}

	public void deleteComment(Long userId, Long boardId, Long commentId) {
		Comment comment = commentRepository.findById(commentId)
			.orElseThrow(() -> new NotFoundException(ErrorMessage.COMMENT_NOT_FOUND));
		Comment parent = comment.getParent();
		Board board = comment.getBoard();

		checkWriter(comment, userId);
		comment.disable();
		deleteCommentIfCan(comment, board);
		if (comment.isReply()) {
			parent.deleteReply(comment);
			deleteCommentIfCan(parent, board);
		}
	}

	private void deleteCommentIfCan(Comment comment, Board board) {
		if (comment.isDisabled() && comment.canDelete()) {
			board.deleteComment(comment);
		}
	}

	private void checkWriter(Comment comment, Long userId) {
		if (!comment.isWriter(userId)) {
			throw new NotMatchException(ErrorMessage.WRITER_NOT_MATCH);
		}
	}
}
