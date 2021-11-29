package com.highgeupsik.backend.api;

import static com.highgeupsik.backend.utils.ApiUtils.*;

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
public class CommentLikeController {

	private final LikeService likeService;

	@ApiOperation(value = "댓글 좋아요")
	@PostMapping("/comments/{commentId}/like")
	public ApiResult pressCommentLike(@LoginUser Long userId, @PathVariable Long commentId) {
		return success(likeService.saveOrUpdateCommentLike(userId, commentId));
	}

	@ApiOperation(value = "좋아요 조회")
	@GetMapping("/comments/{commentId}/like")
	public ApiResult boardLike(@PathVariable("commentId") Long commentId, @LoginUser Long userId) {
		return ApiUtils.success(likeService.isExistedCommentLike(userId, commentId));
	}
}