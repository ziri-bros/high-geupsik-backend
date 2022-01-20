package com.highgeupsik.backend.api;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.highgeupsik.backend.dto.BoardResDTO;
import com.highgeupsik.backend.resolver.LoginUser;
import com.highgeupsik.backend.service.BoardQueryService;
import com.highgeupsik.backend.utils.ApiResult;
import com.highgeupsik.backend.utils.ApiUtils;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class UserBoardController {

    private final BoardQueryService boardQueryService;

    @ApiOperation(value = "내가 작성한 게시글 목록 조회")
    @GetMapping("/boards/my") //내가 작성한 게시글
    public ApiResult<List<BoardResDTO>> myBoards(
        @RequestParam(value = "page", defaultValue = "1") Integer pageNum,
        @LoginUser Long userId) {
        return ApiUtils.success(boardQueryService.findByMyId(userId, pageNum));
    }
}
