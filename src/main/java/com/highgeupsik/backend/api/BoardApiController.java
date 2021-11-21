package com.highgeupsik.backend.api;

import com.highgeupsik.backend.dto.BoardResDTO;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import com.highgeupsik.backend.dto.BoardReqDTO;
import com.highgeupsik.backend.dto.BoardSearchCondition;
import com.highgeupsik.backend.exception.NotMatchException;
import com.highgeupsik.backend.resolver.LoginUser;
import com.highgeupsik.backend.service.BoardQueryService;
import com.highgeupsik.backend.service.BoardService;
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
public class BoardApiController {

    private final BoardQueryService boardQueryService;
    private final BoardService boardService;
    private final LikeService likeService;

    @ApiOperation(value = "게시글 단일 조회")
    @GetMapping("/boards/{boardId}")
    public ApiResult<BoardResDTO> board(@PathVariable("boardId") Long boardId) {
        return ApiUtils.success(boardQueryService.findOneById(boardId));
    }

    @ApiOperation(value = "게시글 목록 조회")
    @GetMapping("/boards")
    public ApiResult<Page<BoardResDTO>> boards(@RequestParam(value = "page", defaultValue = "1") Integer pageNum,
        @LoginUser Long userId, BoardSearchCondition condition) {
        return ApiUtils.success(boardQueryService.findAll(userId, pageNum, condition));
    }

    @ApiOperation(value = "게시글 작성")
    @PostMapping("/boards")
    public ApiResult writeBoard(@LoginUser Long userId,
        @RequestBody BoardReqDTO boardReqDTO) {
        if (!boardReqDTO.getUploadFileDTOList().isEmpty()) {
            return ApiUtils.success(boardService.saveBoard(userId, boardReqDTO));
        }
        return ApiUtils.success(boardService.saveBoard(userId, boardReqDTO.getTitle(),
            boardReqDTO.getContent(), boardReqDTO.getCategory()));
    }

    @ApiOperation(value = "게시글 편집", notes = "게시글 편집 화면으로 넘어가기위해 이전 정보를 리턴")
    @GetMapping("/boards/{boardId}/edit")
    public ApiResult<BoardResDTO> editBoard(@PathVariable("boardId") Long boardId,
        @LoginUser Long userId) {
        BoardResDTO boardResDTO = boardQueryService.findOneById(boardId);
        Long writerId = boardResDTO.getWriterId();
        if (writerId.equals(userId)) {
            return ApiUtils.success(boardResDTO);
        }
        throw new NotMatchException(ErrorMessage.WRITER_NOT_MATCH);
    }

    @ApiOperation(value = "게시글 편집")
    @PutMapping("/boards/{boardId}") //게시글 편집
    public ApiResult editBoard(@PathVariable("boardId") Long boardId,
        @LoginUser Long userId, @RequestBody BoardReqDTO boardReqDTO) {
        Long writerId = boardQueryService.findWriterIdByBoardId(boardId);
        if (writerId.equals(userId)) {
            return ApiUtils.success(boardService.updateBoard(boardId, boardReqDTO));
        }
        throw new NotMatchException(ErrorMessage.WRITER_NOT_MATCH);
    }

    @ApiOperation(value = "게시글 삭제")
    @DeleteMapping("/boards/{boardId}") //게시글 삭제
    public ApiResult deleteBoard(@PathVariable("boardId") Long boardId, @LoginUser Long userId) {
        boardService.deleteBoard(userId, boardId);
        return ApiUtils.success(null);
    }

    @ApiOperation(value = "내가 작성한 게시글 목록 조회")
    @GetMapping("/boards/my") //내가 작성한 게시글
    public ApiResult<List<BoardResDTO>> myBoards(
        @RequestParam(value = "page", defaultValue = "1") Integer pageNum,
        @LoginUser Long userId) {
        return ApiUtils.success(boardQueryService.findByMyId(userId, pageNum));
    }

    @ApiOperation(value = "게시글 좋아요")
    @PostMapping("/boards/{boardId}/like") //게시글 좋아요
    public ApiResult pressBoardLike(@PathVariable("boardId") Long boardId,
        @LoginUser Long userId) {
        return ApiUtils.success(likeService.saveOrUpdateBoardDetailLike(userId, boardId));
    }
}
