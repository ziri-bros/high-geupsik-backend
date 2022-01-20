package com.highgeupsik.backend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.highgeupsik.backend.dto.BoardResDTO;
import com.highgeupsik.backend.dto.BoardSearchCondition;

public interface BoardRepositoryCustom {

    Page<BoardResDTO> findAll(BoardSearchCondition condition, Pageable pageable);
}
