package com.highgeupsik.backend.repository.board;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.highgeupsik.backend.api.board.BoardResDTO;
import com.highgeupsik.backend.api.board.BoardSearchCondition;

public interface BoardRepositoryCustom {

    Page<BoardResDTO> findAll(BoardSearchCondition condition, Pageable pageable);
}
