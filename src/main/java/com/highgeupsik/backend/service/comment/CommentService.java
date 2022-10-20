package com.highgeupsik.backend.service.comment;

import static com.highgeupsik.backend.exception.ErrorMessages.BOARD_NOT_FOUND;
import static com.highgeupsik.backend.exception.ErrorMessages.COMMENT_NOT_FOUND;
import static com.highgeupsik.backend.exception.ErrorMessages.USER_NOT_FOUND;

import com.highgeupsik.backend.service.notification.NotificationService;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.highgeupsik.backend.api.comment.CommentReqDTO;
import com.highgeupsik.backend.api.comment.CommentResDTO;
import com.highgeupsik.backend.entity.board.Board;
import com.highgeupsik.backend.entity.board.Comment;
import com.highgeupsik.backend.entity.notification.NotificationType;
import com.highgeupsik.backend.entity.user.User;
import com.highgeupsik.backend.exception.ResourceNotFoundException;
import com.highgeupsik.backend.repository.board.BoardRepository;
import com.highgeupsik.backend.repository.comment.CommentRepository;
import com.highgeupsik.backend.repository.user.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional
@Service
public class CommentService {

	private final CommentRepository commentRepository;
	private final BoardRepository boardRepository;
	private final UserRepository userRepository;
	private final NotificationService notificationService;
	private static final int BOARD_WRITER_ANONYMOUS_NUMBER = -1;

	public CommentResDTO saveComment(Long userId, Long boardId, CommentReqDTO dto) {
		User commentWriter = userRepository.findById(userId)
			.orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND));
		Board board = boardRepository.findById(boardId)
			.orElseThrow(() -> new ResourceNotFoundException(BOARD_NOT_FOUND));

		Comment comment = Comment.of(dto.getContent(), commentWriter, board,
			getAnonymousNumberFrom(board, commentWriter));
		comment.setBoard(board);
		commentRepository.save(comment);

		Set<Long> sendUserIdList = new HashSet<>();
		if (isReply(dto)) {
			Comment parent = commentRepository.findById(dto.getParentId())
				.orElseThrow(() -> new ResourceNotFoundException(COMMENT_NOT_FOUND));
			initSendIdList(comment, parent, sendUserIdList);
			sendReplyNotification(board, comment, sendUserIdList);
			transformToReply(comment, parent);
		}
		sendCommentNotification(board, comment, sendUserIdList);

		return new CommentResDTO(comment, false);
	}

	private boolean isReply(CommentReqDTO dto) {
		return dto.getParentId() != null;
	}

	private int getAnonymousNumberFrom(Board board, User commentWriter) {
		if (board.isWriter(commentWriter)) {
			return BOARD_WRITER_ANONYMOUS_NUMBER;
		}
		return commentRepository.findFirstByBoardAndUser(board, commentWriter)
			.map(Comment::getAnonymousId)
			.orElseGet(board::getNextAnonymousNumber);
	}

	private void transformToReply(Comment comment, Comment parent) {
		comment.toReply(parent);
	}

	private void sendReplyNotification(Board board, Comment comment, Set<Long> sendUserIdList) {
		sendUserIdList.stream()
			.map(id -> userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND)))
			.forEach((u) -> notificationService.saveCommentNotification(u, board, comment, NotificationType.REPLY));
	}

	private void sendCommentNotification(Board board, Comment comment, Set<Long> sendUserIdList) {
		if (isBoardWriterHasNotReceiveNotification(board, comment, sendUserIdList)) {
			User boardWriter = userRepository.findById(board.getUser().getId())
				.orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND));
			notificationService.saveCommentNotification(boardWriter, board, comment, NotificationType.COMMENT);
		}
	}

	private boolean isBoardWriterHasNotReceiveNotification(Board board, Comment comment, Set<Long> sendUserIdList) {
		return !board.isWriter(comment.getUser()) && !sendUserIdList.contains(board.getUser().getId());
	}

	public Long updateComment(Long userId, Long commentId, CommentReqDTO commentReqDTO) {
		User writer = userRepository.findById(userId)
			.orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND));
		Comment comment = commentRepository.findById(commentId)
			.orElseThrow(() -> new ResourceNotFoundException(COMMENT_NOT_FOUND));
		comment.validateWriter(writer);
		comment.updateContent(commentReqDTO);
		return commentId;
	}

	public void deleteComment(Long userId, Long commentId) {
		User writer = userRepository.findById(userId)
			.orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND));
		Comment comment = commentRepository.findById(commentId)
			.orElseThrow(() -> new ResourceNotFoundException(COMMENT_NOT_FOUND));
		Comment parent = comment.getParent();
		comment.validateWriter(writer);
		comment.disable();

		parent.deleteReply();

		if (parent.isDisabled() && parent.canDelete()) {
			deleteNotifications(parent);
			parent.deleteComment();
		}

		Board board = comment.getBoard();
		board.deleteCommentCount();
	}

	public void deleteNotifications(Comment comment) {
		notificationService.deleteByComment(comment);
	}

	private void initSendIdList(Comment newComment, Comment parent, Set<Long> sendUserIdList) {
		sendForParent(newComment, parent, sendUserIdList);
		sendForChildren(newComment, parent, sendUserIdList);
	}

	private void sendForParent(Comment newComment, Comment parentComment, Set<Long> sendUserIdList) {
		if (parentComment.isNotWriter(newComment.getUser())) {
			sendUserIdList.add(parentComment.getUser().getId());
		}
	}

	private void sendForChildren(Comment newComment, Comment parent, Set<Long> sendUserIdList) {
		sendUserIdList.addAll(parent.getChildren().stream()
			.filter(c -> c.isNotWriter(newComment.getUser()))
			.map(comment -> comment.getUser().getId())
			.collect(Collectors.toList()));
	}
}