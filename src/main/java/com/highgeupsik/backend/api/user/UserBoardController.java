package com.highgeupsik.backend.api.user;

import static com.highgeupsik.backend.api.ApiUtils.*;
import static com.highgeupsik.backend.utils.PagingUtils.DEFAULT_PAGE_NUMBER;

import com.highgeupsik.backend.api.ApiResult;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.highgeupsik.backend.api.board.BoardResDTO;
import com.highgeupsik.backend.resolver.LoginUser;
import com.highgeupsik.backend.service.board.BoardQueryService;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class UserBoardController {

    private final BoardQueryService boardQueryService;

    @ApiOperation(value = "내가 작성한 게시글 목록 조회")
    @GetMapping("/boards/my")
    public ApiResult<Page<BoardResDTO>> myBoards(@LoginUser Long userId,
        @RequestParam(value = "page", defaultValue = DEFAULT_PAGE_NUMBER) Integer pageNum) {
        return success(boardQueryService.findByMyId(userId, pageNum));
    }

    @ApiOperation(value = "내가 댓글단 게시글 목로 조회")
    @GetMapping("boards/my-comments")
    public ApiResult<Page<BoardResDTO>> boardsByMyComments(@LoginUser Long userId,
        @RequestParam(value = "age", defaultValue = DEFAULT_PAGE_NUMBER) Integer pageNum) {
        return success(boardQueryService.findByMyComments(userId, pageNum));
    }

}
