package com.highgeupsik.backend.service;

import static com.highgeupsik.backend.exception.ErrorMessages.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.highgeupsik.backend.entity.board.Board;
import com.highgeupsik.backend.entity.board.Comment;
import com.highgeupsik.backend.entity.board.Like;
import com.highgeupsik.backend.entity.user.User;
import com.highgeupsik.backend.exception.ResourceNotFoundException;
import com.highgeupsik.backend.repository.board.BoardRepository;
import com.highgeupsik.backend.repository.comment.CommentRepository;
import com.highgeupsik.backend.repository.LikeRepository;
import com.highgeupsik.backend.repository.user.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional
@Service
public class LikeService {

    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;

    public boolean saveOrUpdateBoardLike(Long userId, Long boardId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND));
        Board board = boardRepository.findById(boardId)
            .orElseThrow(() -> new ResourceNotFoundException(BOARD_NOT_FOUND));
        Like like = likeRepository.findByUserIdAndBoardId(userId, boardId)
            .map(Like::update).orElse(Like.of(user));
        like.setBoard(board);
        board.updateBoardLikeCount(like.getFlag());
        likeRepository.save(like);
        return like.getFlag();
    }

    public boolean saveOrUpdateCommentLike(Long userId, Long commentId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND));
        Comment comment = commentRepository.findById(commentId)
            .orElseThrow(() -> new ResourceNotFoundException(COMMENT_NOT_FOUND));
        Like like = likeRepository.findByUserIdAndCommentId(userId, commentId)
            .map(Like::update).orElse(Like.of(user));
        like.setComment(comment);
        comment.updateCommentLike(like.getFlag());
        likeRepository.save(like);
        return like.getFlag();
    }
}
