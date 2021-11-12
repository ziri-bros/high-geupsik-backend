package com.highgeupsik.backend.repository;

import com.highgeupsik.backend.entity.Like;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {

    public Optional<Like> findByUserIdAndBoardId(Long userId, Long boardId);

    public Optional<Like> findByUserIdAndCommentId(Long userId, Long commentId);

}
