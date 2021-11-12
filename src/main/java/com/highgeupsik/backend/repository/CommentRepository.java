package com.highgeupsik.backend.repository;

import com.highgeupsik.backend.entity.Comment;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    public Page<Comment> findByUserId(Long userId, Pageable pageable);

    public Page<Comment> findByBoardIdAndParent(Long postId, Comment parent, Pageable pageable);

    public List<Comment> findByUserIdAndBoardId(Long userId, Long postId);

}
