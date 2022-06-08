package com.highgeupsik.backend.service;

import static com.highgeupsik.backend.utils.ErrorMessage.*;

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
        User writer = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND));
        Board board = boardRepository.findById(boardId)
            .orElseThrow(() -> new ResourceNotFoundException(BOARD_NOT_FOUND));

        int anonymousNumber = getAnonymousNumberFrom(board, writer);

        Comment comment = Comment.of(dto.getContent(), writer, board);
        comment.setBoard(board);
        comment.setAnonymousId(anonymousNumber);
        if (board.isWriter(writer.getId())) {
            comment.setAnonymousId(-1);
        }

        Comment savedComment = commentRepository.save(comment);

        if (dto.getParentId() != null) {
            transformToReply(savedComment, dto.getParentId());
            notificationService.saveReplyNotification(writer, savedComment);
        }
        User boardWriter = userRepository.findById(board.getUser().getId())
            .orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND));
        notificationService.saveCommentNotification(boardWriter, board);

        return new CommentResDTO(comment, false);
    }

    private int getAnonymousNumberFrom(Board board, User writer) {
        return commentRepository.findFirstByBoardAndUser(board, writer)
            .map(Comment::getAnonymousId)
            .orElseGet(board::getNextAnonymousNumber);
    }

    private void transformToReply(Comment comment, Long parentId) {
        Comment parent = commentRepository.findById(parentId)
            .orElseThrow(() -> new ResourceNotFoundException(COMMENT_NOT_FOUND));
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
        comment.deleteIfCan();
        if (comment.isReply()) {
            parent.deleteReply(comment);
            parent.deleteIfCan();
        }
    }
}
