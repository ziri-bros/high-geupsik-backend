package com.highgeupsik.backend.service;


import static com.highgeupsik.backend.utils.ErrorMessage.POST_NOT_FOUND;
import static com.highgeupsik.backend.utils.ErrorMessage.USER_NOT_FOUND;
import static com.highgeupsik.backend.utils.PagingUtils.orderByCreatedDateDESC;


import com.highgeupsik.backend.dto.BoardResDTO;
import com.highgeupsik.backend.dto.BoardSearchCondition;
import com.highgeupsik.backend.entity.Board;
import com.highgeupsik.backend.entity.Region;
import com.highgeupsik.backend.exception.NotFoundException;
import com.highgeupsik.backend.repository.BoardRepository;
import com.highgeupsik.backend.repository.UserRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardQueryService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    private static final int POST_COUNT = 20;

    public Long findWriterIdByBoardId(Long boardId) {
        return boardRepository.findById(boardId).orElseThrow(() ->
            new NotFoundException(POST_NOT_FOUND)).getUser().getId();
    }

    public BoardResDTO findOneById(Long postId) {
        return new BoardResDTO(boardRepository.findById(postId).orElseThrow(() ->
            new NotFoundException(POST_NOT_FOUND)));
    }

    public List<BoardResDTO> findByMyId(Long userId, Integer pageNum) {
        List<Board> boards = boardRepository.findByUserId(userId, orderByCreatedDateDESC(
            pageNum, POST_COUNT)).getContent();
        return boards.stream().map((post) -> new BoardResDTO(post)).collect(Collectors.toList());
    }

    public Page<BoardResDTO> findAll(Long userId, Integer pageNum, BoardSearchCondition condition) {
        Region region = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(USER_NOT_FOUND))
            .getSchoolInfo().getRegion();
        condition.setRegion(region);
        return boardRepository.findAll(condition, orderByCreatedDateDESC(pageNum, POST_COUNT));
    }


}
