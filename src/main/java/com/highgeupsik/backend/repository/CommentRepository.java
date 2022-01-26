package com.highgeupsik.backend.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.highgeupsik.backend.entity.Board;
import com.highgeupsik.backend.entity.Comment;
import com.highgeupsik.backend.entity.User;

public interface CommentRepository extends JpaRepository<Comment, Long>, CommentRepositoryCustom {

    @EntityGraph(attributePaths = {"board", "parent"})
    Optional<Comment> findById(Long commentId);

    Optional<Comment> findFirstByBoardAndUser(Board board, User user);

    Page<Comment> findByUserId(Long userId, Pageable pageable);
}
