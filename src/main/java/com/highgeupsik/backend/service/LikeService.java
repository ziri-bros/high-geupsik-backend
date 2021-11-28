package com.highgeupsik.backend.service;


import static com.highgeupsik.backend.utils.ErrorMessage.COMMENT_NOT_FOUND;
import static com.highgeupsik.backend.utils.ErrorMessage.POST_NOT_FOUND;
import static com.highgeupsik.backend.utils.ErrorMessage.USER_NOT_FOUND;

import com.highgeupsik.backend.entity.Board;
import com.highgeupsik.backend.entity.Comment;
import com.highgeupsik.backend.entity.Like;
import com.highgeupsik.backend.entity.User;
import com.highgeupsik.backend.exception.NotFoundException;
import com.highgeupsik.backend.repository.BoardRepository;
import com.highgeupsik.backend.repository.CommentRepository;
import com.highgeupsik.backend.repository.LikeRepository;
import com.highgeupsik.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;

    public Like saveBoardDetailLike(User user, Board board) {
        Like like = likeRepository.save(Like.builder()
            .user(user)
            .build());
        like.setBoard(board);
        return like;
    }

    public Like saveCommentLike(User user, Comment comment) {
        Like like = likeRepository.save(Like.builder()
            .user(user)
            .build());
        like.setComment(comment);
        return like;
    }

    public boolean saveOrUpdateBoardDetailLike(Long userId, Long boardId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));
        Board board = boardRepository.findById(boardId).orElseThrow(
            () -> new NotFoundException(POST_NOT_FOUND));
        Like like = likeRepository.findByUserIdAndBoardId(userId, boardId).map((entity) -> entity.update())
            .orElse(saveBoardDetailLike(user, board));
        board.updateBoardLikeCount(like.getFlag());
        return like.getFlag();
    }

    public boolean saveOrUpdateCommentLike(Long userId, Long commentId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));
        Comment comment = commentRepository.findById(commentId).orElseThrow(
            () -> new NotFoundException(COMMENT_NOT_FOUND));
        Like like = likeRepository.findByUserIdAndCommentId(userId, commentId).map((entity) -> entity.update())
            .orElse(saveCommentLike(user, comment));
        comment.updateCommentLike(like.getFlag());
        return like.getFlag();
    }

    public boolean isExistedBoardLike(Long userId, Long boardId) {
        return likeRepository.findByUserIdAndBoardId(userId, boardId).isPresent();
    }

    public boolean isExistedCommentLike(Long userId, Long commentId) {
        return likeRepository.findByUserIdAndBoardId(userId, commentId).isPresent();
    }

}
