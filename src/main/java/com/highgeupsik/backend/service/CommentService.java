package com.highgeupsik.backend.service;

import static com.highgeupsik.backend.utils.ErrorMessage.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.highgeupsik.backend.dto.CommentReqDTO;
import com.highgeupsik.backend.dto.CommentResDTO;
import com.highgeupsik.backend.entity.Board;
import com.highgeupsik.backend.entity.Comment;
import com.highgeupsik.backend.entity.User;
import com.highgeupsik.backend.exception.NotFoundException;
import com.highgeupsik.backend.repository.BoardRepository;
import com.highgeupsik.backend.repository.CommentRepository;
import com.highgeupsik.backend.repository.UserRepository;

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
            .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));
        Board board = boardRepository.findById(boardId)
            .orElseThrow(() -> new NotFoundException(BOARD_NOT_FOUND));

        int anonymousNumber = getAnonymousNumberFrom(board, writer);

        Comment comment = Comment.of(dto.getContent(), writer, board);
        comment.setBoard(board);
        comment.setAnonymousId(anonymousNumber);
        if (board.isWriter(writer.getId())) {
            comment.setAnonymousId(-1);
        }

        if (dto.getParentId() != null) {
            transformToReply(comment, dto.getParentId());
        }
        commentRepository.save(comment);
        return new CommentResDTO(comment, false);
    }

    private int getAnonymousNumberFrom(Board board, User writer) {
        return commentRepository.findFirstByBoardAndUser(board, writer)
            .map(Comment::getAnonymousId)
            .orElseGet(board::getNextAnonymousNumber);
    }

    private void transformToReply(Comment comment, Long parentId) {
        Comment parent = commentRepository.findById(parentId)
            .orElseThrow(() -> new NotFoundException(COMMENT_NOT_FOUND));
        comment.toReply(parent);
    }

    public Long updateComment(Long userId, Long commentId, CommentReqDTO commentReqDTO) {
        Comment comment = commentRepository.findById(commentId)
            .orElseThrow(() -> new NotFoundException(COMMENT_NOT_FOUND));
        comment.checkWriter(userId);
        comment.updateContent(commentReqDTO);
        return commentId;
    }

    public void deleteComment(Long userId, Long commentId) {
        Comment comment = commentRepository.findById(commentId)
            .orElseThrow(() -> new NotFoundException(COMMENT_NOT_FOUND));
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
