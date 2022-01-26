package com.highgeupsik.backend.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.highgeupsik.backend.resolver.LoginUser;
import com.highgeupsik.backend.service.LikeService;
import com.highgeupsik.backend.utils.ApiResult;
import com.highgeupsik.backend.utils.ApiUtils;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/boards")
@RestController
public class BoardLikeController {

    private final LikeService likeService;

    @ApiOperation(value = "게시글 좋아요")
    @PostMapping("/{boardId}/likes") //게시글 좋아요
    public ApiResult pressBoardLike(@PathVariable("boardId") Long boardId, @LoginUser Long userId) {
        return ApiUtils.success(likeService.saveOrUpdateBoardLike(userId, boardId));
    }

    @ApiOperation(value = "게시글 좋아요 조회")
    @GetMapping("/{boardId}/likes")
    public ApiResult boardLike(@PathVariable("boardId") Long boardId, @LoginUser Long userId) {
        return ApiUtils.success(likeService.findBoardLikeDTO(userId, boardId));
    }
}
