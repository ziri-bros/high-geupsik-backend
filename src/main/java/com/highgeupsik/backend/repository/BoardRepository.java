package com.highgeupsik.backend.repository;

import com.highgeupsik.backend.entity.Board;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long>, BoardRepositoryCustom {

    Page<Board> findByUserId(Long userId, Pageable pageable);

    @EntityGraph(attributePaths = {"uploadFileList"})
    Optional<Board> findById(Long boardId);
}
