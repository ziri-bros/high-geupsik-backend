package com.highgeupsik.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.highgeupsik.backend.entity.Board;
import com.highgeupsik.backend.entity.Comment;
import com.highgeupsik.backend.entity.User;

public interface CommentRepository extends JpaRepository<Comment, Long>, CommentRepositoryCustom {

    @EntityGraph(attributePaths = {"board", "parent"})
    Optional<Comment> findById(Long commentId);

    List<Comment> findByUserIdAndBoardId(Long userId, Long boardId);

    Optional<Comment> findFirstByBoardAndUser(Board board, User user);
}
