package com.highgeupsik.backend.api;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import com.highgeupsik.backend.dto.BoardDetailReqDTO;
import com.highgeupsik.backend.dto.BoardDetailResDTO;
import com.highgeupsik.backend.dto.BoardSearchCondition;
import com.highgeupsik.backend.exception.NotMatchException;
import com.highgeupsik.backend.resolver.LoginUser;
import com.highgeupsik.backend.service.BoardDetailQueryService;
import com.highgeupsik.backend.service.BoardDetailService;
import com.highgeupsik.backend.service.LikeService;
import com.highgeupsik.backend.utils.ApiResult;
import com.highgeupsik.backend.utils.ApiUtils;
import com.highgeupsik.backend.utils.ErrorMessage;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BoardDetailApiController {

    private final BoardDetailQueryService boardDetailQueryService;
    private final BoardDetailService boardDetailService;
    private final LikeService likeService;

    @GetMapping("/boards/{boardId}") //게시글 조회
    public ApiResult<BoardDetailResDTO> board(@PathVariable("boardId") Long boardId) {
        return ApiUtils.success(boardDetailQueryService.findOneById(boardId));
    }

    @GetMapping("/boards") //게시글목록 조회
    public ApiResult<Page<BoardDetailResDTO>> boards(@RequestParam(value = "page", defaultValue = "1") Integer pageNum,
        @LoginUser Long userId, BoardSearchCondition condition) {
        return ApiUtils.success(boardDetailQueryService.findAll(userId, pageNum, condition));
    }

    @PostMapping("/boards") //게시글 작성
    public ApiResult writeBoard(@LoginUser Long userId,
        @RequestBody BoardDetailReqDTO boardDetailReqDTO) {
        if (!boardDetailReqDTO.getUploadFileDTOList().isEmpty()) {
            return ApiUtils.success(boardDetailService.savePost(userId, boardDetailReqDTO));
        }
        return ApiUtils.success(boardDetailService.savePost(userId, boardDetailReqDTO.getTitle(),
            boardDetailReqDTO.getContent(), boardDetailReqDTO.getCategory()));
    }

    @GetMapping("/boards/{boardId}/edit") //게시글 편집창
    public ApiResult<BoardDetailResDTO> editBoard(@PathVariable("boardId") Long boardId,
        @LoginUser Long userId) {
        BoardDetailResDTO boardDetailResDTO = boardDetailQueryService.findOneById(boardId);
        Long writerId = boardDetailResDTO.getWriterId();
        if (writerId.equals(userId)) {
            return ApiUtils.success(boardDetailResDTO);
        }
        throw new NotMatchException(ErrorMessage.WRITER_NOT_MATCH);
    }

    @PutMapping("/boards/{boardId}") //게시글 편집
    public ApiResult editBoard(@PathVariable("boardId") Long boardId,
        @LoginUser Long userId, @RequestBody BoardDetailReqDTO boardDetailReqDTO) {
        Long writerId = boardDetailQueryService.findWriterIdByBoardId(boardId);
        if (writerId.equals(userId)) {
            return ApiUtils.success(boardDetailService.updatePost(boardId, boardDetailReqDTO));
        }
        throw new NotMatchException(ErrorMessage.WRITER_NOT_MATCH);
    }

    @DeleteMapping("/boards/{boardId}") //게시글 삭제
    public ApiResult deleteBoard(@PathVariable("boardId") Long boardId, @LoginUser Long userId) {
        boardDetailService.deletePost(userId, boardId);
        return ApiUtils.success(null);
    }

    @GetMapping("/boards/my") //내가 작성한 게시글
    public ApiResult<List<BoardDetailResDTO>> myBoards(
        @RequestParam(value = "page", defaultValue = "1") Integer pageNum,
        @LoginUser Long userId) {
        return ApiUtils.success(boardDetailQueryService.findByMyId(userId, pageNum));
    }

    @PostMapping("/boards/{boardId}/like") //게시글 좋아요
    public ApiResult pressBoardLike(@PathVariable("boardId") Long boardId,
        @LoginUser Long userId) {
        return ApiUtils.success(likeService.saveOrUpdateBoardDetailLike(userId, boardId));
    }
}
