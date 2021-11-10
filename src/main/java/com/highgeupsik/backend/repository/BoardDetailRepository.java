package com.highgeupsik.backend.repository;

import com.highgeupsik.backend.entity.BoardDetail;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardDetailRepository extends JpaRepository<BoardDetail, Long>, BoardDetailRepositoryCustom {

    public Page<BoardDetail> findByUserId(Long userId, Pageable pageable);

    @EntityGraph(attributePaths = {"uploadFileList"})
    public Optional<BoardDetail> findById(Long boardId);

}
