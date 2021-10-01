package com.highgeupsik.backend.service;


import com.highgeupsik.backend.dto.BoardDetailResDTO;
import com.highgeupsik.backend.dto.BoardSearchCondition;
import com.highgeupsik.backend.entity.BoardDetail;
import com.highgeupsik.backend.entity.Region;
import com.highgeupsik.backend.exception.NotFoundException;
import com.highgeupsik.backend.repository.BoardDetailRepository;
import com.highgeupsik.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.highgeupsik.backend.utils.ErrorMessage.*;
import static com.highgeupsik.backend.utils.PagingUtils.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardDetailQueryService {

    private final BoardDetailRepository boardDetailRepository;
    private final UserRepository userRepository;

    private static final int POST_COUNT = 20;

    public Long findWriterIdByBoardId(Long boardId) {
        return boardDetailRepository.findById(boardId).orElseThrow(() ->
                new NotFoundException(POST_NOT_FOUND)).getUser().getId();
    }

    public BoardDetailResDTO findOneById(Long postId) {
        return new BoardDetailResDTO(boardDetailRepository.findById(postId).orElseThrow(() ->
                new NotFoundException(POST_NOT_FOUND)));
    }

    public List<BoardDetailResDTO> findByMyId(Long userId, Integer pageNum) {
        List<BoardDetail> boardDetails = boardDetailRepository.findByUserId(userId, orderByCreatedDateDESC(
                pageNum, POST_COUNT)).getContent();
        return boardDetails.stream().map((post) -> new BoardDetailResDTO(post)).collect(Collectors.toList());
    }

    public Page<BoardDetailResDTO> findAll(Long userId, Integer pageNum, BoardSearchCondition condition) {
        Region region = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(USER_NOT_FOUND))
                .getSchoolInfo().getRegion();
        condition.setRegion(region);
        return boardDetailRepository.findAll(condition, orderByCreatedDateDESC(pageNum, POST_COUNT));
    }

}
