package com.highgeupsik.backend.service;


import static com.highgeupsik.backend.utils.ErrorMessage.COMMENT_NOT_FOUND;
import static com.highgeupsik.backend.utils.ErrorMessage.LIKE_NOT_FOUND;
import static com.highgeupsik.backend.utils.ErrorMessage.POST_NOT_FOUND;
import static com.highgeupsik.backend.utils.ErrorMessage.USER_NOT_FOUND;

import com.highgeupsik.backend.dto.LikeDTO;
import com.highgeupsik.backend.entity.Board;
import com.highgeupsik.backend.entity.Comment;
import com.highgeupsik.backend.entity.Like;
import com.highgeupsik.backend.entity.User;
import com.highgeupsik.backend.exception.NotFoundException;
import com.highgeupsik.backend.repository.BoardRepository;
import com.highgeupsik.backend.repository.CommentRepository;
import com.highgeupsik.backend.repository.LikeRepository;
import com.highgeupsik.backend.repository.UserRepository;
import java.util.Optional;
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

    public Like saveBoardLike(User user, Board board) {
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
        Optional<Like> boardLike = findBoardLike(userId, boardId);
        if (!boardLike.isPresent()) {
            Like like = saveBoardLike(user, board);
            board.updateBoardLikeCount(like.getFlag());
            return like.getFlag();
        } else {
            Like like = boardLike.get();
            like.update();
            board.updateBoardLikeCount(like.getFlag());
            return like.getFlag();
        }
    }

    public boolean saveOrUpdateCommentLike(Long userId, Long commentId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));
        Comment comment = commentRepository.findById(commentId).orElseThrow(
            () -> new NotFoundException(COMMENT_NOT_FOUND));
        Optional<Like> commentLike = findCommentLike(userId, commentId);
        if (!commentLike.isPresent()) {
            Like like = saveCommentLike(user, comment);
            comment.updateCommentLike(like.getFlag());
            return like.getFlag();
        } else {
            Like like = commentLike.get();
            like.update();
            comment.updateCommentLike(like.getFlag());
            return like.getFlag();
        }
    }

    public Optional<Like> findBoardLike(Long userId, Long boardId) {
        return likeRepository.findByUserIdAndBoardId(userId, boardId);
    }

    public Optional<Like> findCommentLike(Long userId, Long commentId) {
        return likeRepository.findByUserIdAndCommentId(userId, commentId);
    }

    public LikeDTO findBoardLikeDTO(Long userId, Long boardId) {
        return new LikeDTO(likeRepository.findByUserIdAndBoardId(userId, boardId).orElseThrow(
            () -> new NotFoundException(LIKE_NOT_FOUND)));
    }

}
