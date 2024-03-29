package com.highgeupsik.backend.service.board;

import static com.highgeupsik.backend.exception.ErrorMessages.*;
import static com.highgeupsik.backend.utils.PagingUtils.*;

import com.highgeupsik.backend.entity.board.Like;
import com.highgeupsik.backend.repository.LikeRepository;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.highgeupsik.backend.api.board.BoardResDTO;
import com.highgeupsik.backend.api.board.BoardSearchCondition;
import com.highgeupsik.backend.exception.ResourceNotFoundException;
import com.highgeupsik.backend.repository.board.BoardRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class BoardQueryService {

    private final BoardRepository boardRepository;
    private final LikeRepository likeRepository;

    private static final int POST_COUNT = 20;

    public BoardResDTO findOneById(Long userId, Long boardId) {
        return new BoardResDTO(boardRepository.findById(boardId)
            .orElseThrow(() -> new ResourceNotFoundException(BOARD_NOT_FOUND)), isUserLike(userId, boardId));
    }

    public Page<BoardResDTO> findByMyId(Long userId, Integer pageNum) {
        return boardRepository.findByUserIdAndDeleteFlag(userId, false, orderByCreatedDateDESC(pageNum, POST_COUNT))
            .map(BoardResDTO::new);
    }

    public Page<BoardResDTO> findByMyComments(Long userId, Integer pageNum) {
        return boardRepository.findAllByUserComment(userId, orderByCreatedDateDESC(pageNum, POST_COUNT))
            .map(BoardResDTO::new);
    }

    public Page<BoardResDTO> findAll(Integer pageNum, BoardSearchCondition condition) {
        return boardRepository.findAll(condition, orderByCreatedDateDESC(pageNum, POST_COUNT));
    }

    public boolean isUserLike(Long userId, Long boardId) {
        Optional<Like> like = likeRepository.findByUserIdAndBoardId(userId, boardId);
        if (like.isEmpty()) {
            return false;
        }
        return like.get().getFlag();
    }
}
