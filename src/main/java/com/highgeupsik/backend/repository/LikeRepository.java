package com.highgeupsik.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.highgeupsik.backend.entity.board.Comment;
import com.highgeupsik.backend.entity.board.Like;

public interface LikeRepository extends JpaRepository<Like, Long> {

    Optional<Like> findByUserIdAndBoardId(Long userId, Long boardId);

    Optional<Like> findByUserIdAndCommentId(Long userId, Long commentId);

    List<Like> findAllByUserIdAndCommentIn(Long userId, List<Comment> comments);
}
