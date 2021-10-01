package com.highgeupsik.backend.repository;

import com.highgeupsik.backend.dto.BoardDetailResDTO;
import com.highgeupsik.backend.dto.BoardSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface BoardDetailRepositoryCustom {

    Page<BoardDetailResDTO> findAll(BoardSearchCondition condition, Pageable pageable);

}
