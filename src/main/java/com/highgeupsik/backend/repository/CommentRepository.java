package com.highgeupsik.backend.repository;

import com.highgeupsik.backend.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    public Page<Comment> findByUserId(Long userId, Pageable pageable);

    public Page<Comment> findByBoardDetailIdAndParent(Long postId, Comment parent, Pageable pageable);

    public List<Comment> findByUserIdAndBoardDetailId(Long userId, Long postId);

}
