package com.highgeupsik.backend.service;

import static com.highgeupsik.backend.utils.ErrorMessage.*;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.highgeupsik.backend.dto.CommentReqDTO;
import com.highgeupsik.backend.dto.CommentResDTO;
import com.highgeupsik.backend.entity.Board;
import com.highgeupsik.backend.entity.Comment;
import com.highgeupsik.backend.entity.User;
import com.highgeupsik.backend.exception.ResourceNotFoundException;
import com.highgeupsik.backend.repository.BoardRepository;
import com.highgeupsik.backend.repository.CommentRepository;
import com.highgeupsik.backend.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    public CommentResDTO saveComment(Long userId, Long boardId, CommentReqDTO dto) {
        User commentWriter = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND));
        Board board = boardRepository.findById(boardId)
            .orElseThrow(() -> new ResourceNotFoundException(BOARD_NOT_FOUND));

        int anonymousNumber = getAnonymousNumberFrom(board, commentWriter);
        Comment comment = Comment.of(dto.getContent(), commentWriter, board);
        comment.setBoard(board);
        comment.setAnonymousId(anonymousNumber);
        if (board.isWriter(commentWriter.getId())) {
            comment.setAnonymousId(-1);
        }

        User boardWriter = userRepository.findById(board.getUser().getId())
            .orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND));

        Comment newComment = commentRepository.save(comment);
        List<Long> sendList = new ArrayList<>();
        if (dto.getParentId() != null) {
            Comment parent = commentRepository.findById(dto.getParentId())
                .orElseThrow(() -> new ResourceNotFoundException(COMMENT_NOT_FOUND));
            saveReplyNotification(sendList, newComment, board, parent);
            transformToReply(newComment, parent);
        }

        if (!board.isWriter(userId) && !sendList.contains(boardWriter.getId())) {
            notificationService.saveCommentNotification(boardWriter, board, newComment.getContent());
        }

        return new CommentResDTO(comment, false);
    }

    private int getAnonymousNumberFrom(Board board, User writer) {
        return commentRepository.findFirstByBoardAndUser(board, writer)
            .map(Comment::getAnonymousId)
            .orElseGet(board::getNextAnonymousNumber);
    }

    private void transformToReply(Comment comment, Comment parent) {
        comment.toReply(parent);
    }

    public Long updateComment(Long userId, Long commentId, CommentReqDTO commentReqDTO) {
        Comment comment = commentRepository.findById(commentId)
            .orElseThrow(() -> new ResourceNotFoundException(COMMENT_NOT_FOUND));
        comment.checkWriter(userId);
        comment.updateContent(commentReqDTO);
        return commentId;
    }

    public void deleteComment(Long userId, Long commentId) {
        Comment comment = commentRepository.findById(commentId)
            .orElseThrow(() -> new ResourceNotFoundException(COMMENT_NOT_FOUND));
        Comment parent = comment.getParent();

        comment.checkWriter(userId);
        comment.disable();
        if (comment.isReply()) {
            parent.deleteReply();
            parent.deleteIfCan();
        } else {
            comment.deleteIfCan();
        }
        Board board = comment.getBoard();
        board.deleteCommentCount();
    }

    public void saveReplyNotification(List<Long> sendList, Comment newComment, Board board,
        Comment parent) {
        Long newCommentWriterId = newComment.getUser().getId();
        Long parentCommentWriterId = parent.getUser().getId();
        if (isNewCommentWriterNotEqualsCommentWriter(newCommentWriterId, parentCommentWriterId)) {
            saveNotification(parentCommentWriterId, board, newComment);
            sendList.add(parentCommentWriterId);
        }
        List<Comment> children = parent.getChildren();
        for (Comment comment : children) {
            Long commentWriterId = comment.getUser().getId();
            if (isNewCommentWriterNotEqualsCommentWriter(newCommentWriterId, commentWriterId) &&
                !sendList.contains(commentWriterId)) {
                sendList.add(commentWriterId);
                saveNotification(commentWriterId, board, newComment);
            }
        }
    }

    public void saveNotification(Long notificationReceiverUserId, Board board, Comment comment) {
        User user = userRepository.findById(notificationReceiverUserId)
            .orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND));
        notificationService.saveReplyNotification(user, board, comment);
    }

    public boolean isNewCommentWriterNotEqualsCommentWriter(Long writerId, Long commentWriterId) {
        return !writerId.equals(commentWriterId);
    }
}
