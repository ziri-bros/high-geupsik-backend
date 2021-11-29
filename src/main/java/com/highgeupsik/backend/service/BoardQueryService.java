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
import com.highgeupsik.backend.entity.Region;
import com.highgeupsik.backend.exception.NotFoundException;
import com.highgeupsik.backend.repository.BoardRepository;
import com.highgeupsik.backend.repository.UserCardRepository;

import lombok.RequiredArgsConstructor;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class BoardQueryService {

	private final BoardRepository boardRepository;
	private final UserCardRepository userCardRepository;

	private static final int POST_COUNT = 20;

	public Long findWriterIdByBoardId(Long boardId) {
		return boardRepository.findById(boardId).orElseThrow(() -> new NotFoundException(POST_NOT_FOUND)).getUser().getId();
	}

	public BoardResDTO findOneById(Long postId) {
		return new BoardResDTO(
			boardRepository.findById(postId).orElseThrow(() -> new NotFoundException(POST_NOT_FOUND)));
	}

	public List<BoardResDTO> findByMyId(Long userId, Integer pageNum) {
		List<Board> boards = boardRepository.findByUserId(userId, orderByCreatedDateDESC(pageNum, POST_COUNT))
			.getContent();
		return boards.stream().map(BoardResDTO::new).collect(Collectors.toList());
	}

	public Page<BoardResDTO> findAll(Long userId, Integer pageNum, BoardSearchCondition condition) {
		Region region = userCardRepository.findByUserId(userId).orElseThrow(
			() -> new NotFoundException(CARD_NOT_FOUND)).getSchool().getRegion();
		condition.setRegion(region);
		return boardRepository.findAll(condition, orderByCreatedDateDESC(pageNum, POST_COUNT));
	}

}
