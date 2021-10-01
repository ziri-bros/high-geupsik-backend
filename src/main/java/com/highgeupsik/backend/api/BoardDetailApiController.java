package com.highgeupsik.backend.api;

import com.highgeupsik.backend.dto.BoardDetailResDTO;
import com.highgeupsik.backend.dto.BoardDetailReqDTO;
import com.highgeupsik.backend.dto.BoardSearchCondition;
import com.highgeupsik.backend.entity.UploadFile;
import com.highgeupsik.backend.exception.NotMatchException;
import com.highgeupsik.backend.resolver.LoginUser;
import com.highgeupsik.backend.service.BoardDetailQueryService;
import com.highgeupsik.backend.service.BoardDetailService;
import com.highgeupsik.backend.service.LikeService;
import com.highgeupsik.backend.service.S3Service;
import com.highgeupsik.backend.utils.ApiResult;
import com.highgeupsik.backend.utils.ApiUtils;
import com.highgeupsik.backend.utils.ErrorMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class BoardDetailApiController {

    private final BoardDetailQueryService boardDetailQueryService;
    private final BoardDetailService boardDetailService;
    private final LikeService likeService;
    private final S3Service s3Service;

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
                                BoardDetailReqDTO boardDetailReqDTO) throws IOException {
        if (!boardDetailReqDTO.getBoardImageList().isEmpty()) {
            List<UploadFile> uploadFileList = s3Service.uploadFiles(boardDetailReqDTO.getBoardImageList());
            return ApiUtils.success(boardDetailService.savePost(userId, boardDetailReqDTO.getTitle(),
                    boardDetailReqDTO.getContent(), boardDetailReqDTO.getCategory(), uploadFileList));
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
                               @LoginUser Long userId, BoardDetailReqDTO boardDetailReqDTO) throws IOException {
        if (!boardDetailReqDTO.getBoardImageList().isEmpty()) {
            boardDetailService.deleteFilesInPost(userId, boardId);
            List<UploadFile> uploadFileList = s3Service.uploadFiles(boardDetailReqDTO.getBoardImageList());
            boardDetailService.updatePost(userId, boardId, boardDetailReqDTO.getTitle(), boardDetailReqDTO.getContent(),
                    uploadFileList);
        }
        boardDetailService.updatePost(userId, boardId, boardDetailReqDTO.getTitle(), boardDetailReqDTO.getContent());
        return ApiUtils.success(boardId);
    }

    @DeleteMapping("/boards/{boardId}") //게시글 삭제
    public ApiResult deleteBoard(@PathVariable("boardId") Long boardId, @LoginUser Long userId) {
        boardDetailService.deletePost(userId, boardId);
        return ApiUtils.success(null);
    }

    @GetMapping("/boards/my") //내가 작성한 게시글
    public ApiResult<List<BoardDetailResDTO>> myBoards(@RequestParam(value = "page", defaultValue = "1") Integer pageNum,
                                                       @LoginUser Long userId) {
        return ApiUtils.success(boardDetailQueryService.findByMyId(userId, pageNum));
    }

    @PostMapping("/boards/{boardId}/like") //게시글 좋아요
    public ApiResult pressBoardLike(@PathVariable("boardId") Long boardId,
                                    @LoginUser Long userId) {
        return ApiUtils.success(likeService.saveOrUpdateBoardDetailLike(userId, boardId));
    }
}
