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

		Comment comment = commentRepository.save(
			Comment.builder()
				.content(dto.getContent())
				.user(writer)
				.board(board)
				.build()
		);

		comment.setAnonymousNumber(getAnonymousNumberFrom(board, writer));
		if (board.isWriter(writer)) {
			comment.setAnonymousNumber(-1);
		}

		comment.transformToParent();
		if (dto.getParentId() != null) {
			transformToReply(comment, dto.getParentId());
		}

		return new CommentResDTO(comment, false);
	}

	private int getAnonymousNumberFrom(Board board, User writer) {
		return commentRepository.findFirstByBoardAndUser(board, writer)
			.map(Comment::getAnonymousNumber)
			.orElse(board.getNextAnonymousNumber());
	}

	private void transformToReply(Comment reply, Long parentId) {
		Comment parent = commentRepository.findById(parentId)
			.orElseThrow(() -> new NotFoundException(ErrorMessage.COMMENT_NOT_FOUND));
		reply.setParent(parent);
	}

	public Long updateComment(Long userId, Long commentId, CommentReqDTO commentReqDTO) {
		Comment comment = commentRepository.findById(commentId)
			.orElseThrow(() -> new NotFoundException(ErrorMessage.COMMENT_NOT_FOUND));
		Long writerId = comment.getUser().getId();
		if (userId.equals(writerId)) {
			comment.updateContent(commentReqDTO);
			return commentId;
		}
		throw new NotMatchException(ErrorMessage.WRITER_NOT_MATCH);
	}

	public void deleteComment(Long userId, Long commentId, Long boardId) {
		Comment comment = commentRepository.findById(commentId)
			.orElseThrow(() -> new NotFoundException(ErrorMessage.COMMENT_NOT_FOUND));
		Long writerId = comment.getUser().getId();
		if (userId.equals(writerId)) {
			comment.delete();
			boardRepository.findById(boardId)
				.orElseThrow(() -> new NotFoundException(ErrorMessage.BOARD_NOT_FOUND))
				.updateBoardCommentCount(false);
		} else {
			throw new NotMatchException(ErrorMessage.WRITER_NOT_MATCH);
		}
	}

}
