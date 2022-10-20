package com.highgeupsik.backend.api.comment;

import static com.highgeupsik.backend.api.ApiUtils.*;

import com.highgeupsik.backend.api.ApiResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.highgeupsik.backend.resolver.LoginUser;
import com.highgeupsik.backend.service.LikeService;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class CommentLikeController {

    private final LikeService likeService;

    @ApiOperation(value = "댓글 좋아요")
    @PostMapping("/comments/{commentId}/likes")
    public ApiResult<Boolean> saveCommentLike(@LoginUser Long userId, @PathVariable Long commentId) {
        return success(likeService.saveOrUpdateCommentLike(userId, commentId));
    }
}
