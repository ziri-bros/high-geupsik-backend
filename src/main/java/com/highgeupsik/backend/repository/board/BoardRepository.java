package com.highgeupsik.backend.repository.board;

import com.highgeupsik.backend.entity.board.Board;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BoardRepository extends JpaRepository<Board, Long>, BoardRepositoryCustom {

    Page<Board> findByUserIdAndDeleteFlag(Long userId, boolean deleteFlag, Pageable pageable);

    @EntityGraph(attributePaths = {"uploadFileList"})
    Optional<Board> findById(Long boardId);

    @Query("select b from Board b join Comment c on c.board.id = b.id where c.user.id = :userId group by b.id")
    Page<Board> findAllByUserComment(Long userId, Pageable pageable);
}
