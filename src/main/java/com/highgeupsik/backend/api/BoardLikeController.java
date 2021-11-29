package com.highgeupsik.backend.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.highgeupsik.backend.resolver.LoginUser;
import com.highgeupsik.backend.service.LikeService;
import com.highgeupsik.backend.utils.ApiResult;
import com.highgeupsik.backend.utils.ApiUtils;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class BoardLikeController {

	private final LikeService likeService;

	@ApiOperation(value = "게시글 좋아요")
	@PostMapping("/boards/{boardId}/like") //게시글 좋아요
	public ApiResult pressBoardLike(@PathVariable("boardId") Long boardId, @LoginUser Long userId) {
		return ApiUtils.success(likeService.saveOrUpdateBoardDetailLike(userId, boardId));
	}

	@ApiOperation(value = "게시글 좋아요 조회")
	@GetMapping("/boards/{boardId}/like")
	public ApiResult boardLike(@PathVariable("boardId") Long boardId, @LoginUser Long userId) {
		return ApiUtils.success(likeService.isExistedBoardLike(userId, boardId));
	}
}
