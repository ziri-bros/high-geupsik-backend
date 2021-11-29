package com.highgeupsik.backend.repository;

import com.highgeupsik.backend.entity.Board;
import com.highgeupsik.backend.entity.Comment;
import com.highgeupsik.backend.entity.User;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long>, CommentRepositoryCustom {

    Page<Comment> findByUserId(Long userId, Pageable pageable);

    Page<Comment> findByBoardIdAndParent(Long postId, Comment parent, Pageable pageable);

    List<Comment> findByUserIdAndBoardId(Long userId, Long postId);

    Optional<Comment> findFirstByBoardAndUser(Board board, User user);
}
