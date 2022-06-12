package com.highgeupsik.backend.api;

import static com.highgeupsik.backend.utils.ApiUtils.*;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.highgeupsik.backend.dto.BoardResDTO;
import com.highgeupsik.backend.resolver.LoginUser;
import com.highgeupsik.backend.service.BoardQueryService;
import com.highgeupsik.backend.utils.ApiResult;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class UserBoardController {

    private final BoardQueryService boardQueryService;

    @ApiOperation(value = "내가 작성한 게시글 목록 조회")
    @GetMapping("/boards/my")
    public ApiResult<Page<BoardResDTO>> myBoards(@LoginUser Long userId,
        @RequestParam(value = "page", defaultValue = "1") Integer pageNum) {
        return success(boardQueryService.findByMyId(userId, pageNum));
    }

    @ApiOperation(value = "내가 댓글단 게시글 목로 조회")
    @GetMapping("boards/my-comments")
    public ApiResult<Page<BoardResDTO>> boardsByMyComments(@LoginUser Long userId,
        @RequestParam(value = "age", defaultValue = "1") Integer pageNum) {
        return success(boardQueryService.findByMyComments(userId, pageNum));
    }

}
