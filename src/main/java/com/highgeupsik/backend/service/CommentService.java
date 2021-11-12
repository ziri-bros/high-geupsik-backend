package com.highgeupsik.backend.service;

import com.highgeupsik.backend.dto.CommentReqDTO;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;


    public Long saveComment(Long userId, String content, Long postId) {
        Board board = boardRepository.findById(postId)
            .orElseThrow(() -> new NotFoundException(ErrorMessage.POST_NOT_FOUND));
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new NotFoundException(ErrorMessage.USER_NOT_FOUND));
        board.updateBoardCommentCount(true);
        board.updateBoardUserCount();
        Comment comment = commentRepository.save(Comment.builder()
            .user(user)
            .userCount(board.getUserCount())
            .content(content)
            .build());
        comment.setBoard(board);
        return comment.getId();
    }

    public Long saveComment(Long userId, String content, Long postId, int userCount) {
        Board board = boardRepository.findById(postId)
            .orElseThrow(() -> new NotFoundException(ErrorMessage.POST_NOT_FOUND));
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new NotFoundException(ErrorMessage.USER_NOT_FOUND));
        board.updateBoardCommentCount(true);
        Comment comment = commentRepository.save(Comment.builder()
            .user(user)
            .userCount(userCount)
            .content(content)
            .build());
        comment.setBoard(board);
        return comment.getId();
    }

    public Long saveReplyComment(Long userId, String content, Long boardId, Long postId) {
        Board board = boardRepository.findById(postId)
            .orElseThrow(() -> new NotFoundException(ErrorMessage.POST_NOT_FOUND));
        Comment parent = commentRepository.findById(boardId)
            .orElseThrow(() -> new NotFoundException(ErrorMessage.COMMENT_NOT_FOUND));
        board.updateBoardCommentCount(true);
        board.updateBoardUserCount();
        if (parent.getParent() != null) {
            parent = parent.getParent();
        }
        Comment now = commentRepository.save(Comment.builder()
            .user(userRepository.findById(userId).get())
            .userCount(board.getUserCount())
            .board(board)
            .content(content)
            .build());
        now.setParent(parent);
        return now.getId();
    }

    public Long saveReplyComment(Long userId, String content, Long parentId, Long boardId, int userCount) {
        Board board = boardRepository.findById(boardId)
            .orElseThrow(() -> new NotFoundException(ErrorMessage.POST_NOT_FOUND));
        Comment parent = commentRepository.findById(parentId)
            .orElseThrow(() -> new NotFoundException(ErrorMessage.COMMENT_NOT_FOUND));
        board.updateBoardCommentCount(true);
        if (parent.getParent() != null) {
            parent = parent.getParent();
        }
        Comment now = commentRepository.save(Comment.builder()
            .user(userRepository.findById(userId).get())
            .userCount(userCount)
            .board(board)
            .content(content)
            .build());
        now.setParent(parent);
        return now.getId();
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
                .orElseThrow(() -> new NotFoundException(ErrorMessage.POST_NOT_FOUND))
                .updateBoardCommentCount(false);
        } else {
            throw new NotMatchException(ErrorMessage.WRITER_NOT_MATCH);
        }
    }

}
