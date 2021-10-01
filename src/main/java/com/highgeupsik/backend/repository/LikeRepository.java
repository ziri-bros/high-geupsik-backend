package com.highgeupsik.backend.repository;

import com.highgeupsik.backend.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {

    public Optional<Like> findByUserIdAndBoardDetailId(Long userId, Long postId);

    public Optional<Like> findByUserIdAndCommentId(Long userId, Long commentId);

}
