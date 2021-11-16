package com.highgeupsik.backend.repository;


import com.highgeupsik.backend.dto.BoardResDTO;
import com.highgeupsik.backend.dto.BoardSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface BoardRepositoryCustom {

    Page<BoardResDTO> findAll(BoardSearchCondition condition, Pageable pageable);

}
