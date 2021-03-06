package com.highgeupsik.backend.service;

import static com.highgeupsik.backend.utils.ErrorMessage.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.highgeupsik.backend.entity.Board;
import com.highgeupsik.backend.entity.Comment;
import com.highgeupsik.backend.entity.Like;
import com.highgeupsik.backend.entity.User;
import com.highgeupsik.backend.exception.ResourceNotFoundException;
import com.highgeupsik.backend.repository.BoardRepository;
import com.highgeupsik.backend.repository.CommentRepository;
import com.highgeupsik.backend.repository.LikeRepository;
import com.highgeupsik.backend.repository.UserRepository;

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
