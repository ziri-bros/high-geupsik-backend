package com.highgeupsik.backend.service;

import static com.highgeupsik.backend.utils.ErrorMessage.*;
import static com.highgeupsik.backend.utils.PagingUtils.*;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.highgeupsik.backend.dto.BoardResDTO;
import com.highgeupsik.backend.dto.BoardSearchCondition;
import com.highgeupsik.backend.entity.Board;
import com.highgeupsik.backend.exception.NotFoundException;
import com.highgeupsik.backend.repository.BoardRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class BoardQueryService {

    private final BoardRepository boardRepository;

    private static final int POST_COUNT = 20;

    public BoardResDTO findOneById(Long boardId) {
        return new BoardResDTO(
            boardRepository.findById(boardId).orElseThrow(() -> new NotFoundException(BOARD_NOT_FOUND)));
    }

    public List<BoardResDTO> findByMyId(Long userId, Integer pageNum) {
        List<Board> boards = boardRepository.findByUserId(userId, orderByCreatedDateDESC(pageNum, POST_COUNT))
            .getContent();
        return boards.stream().map(BoardResDTO::new).collect(Collectors.toList());
    }

    public Page<BoardResDTO> findAll(Integer pageNum, BoardSearchCondition condition) {
        return boardRepository.findAll(condition, orderByCreatedDateDESC(pageNum, POST_COUNT));
    }
}
