package com.highgeupsik.backend.api.board;

import static com.highgeupsik.backend.api.ApiUtils.*;

import com.highgeupsik.backend.api.ApiResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.highgeupsik.backend.resolver.LoginUser;
import com.highgeupsik.backend.service.LikeService;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/boards")
@RestController
public class BoardLikeController {

    private final LikeService likeService;

    @ApiOperation(value = "게시글 좋아요")
    @PostMapping("/{boardId}/likes")
    public ApiResult<Boolean> saveBoardLike(@LoginUser Long userId, @PathVariable Long boardId) {
        return success(likeService.saveOrUpdateBoardLike(userId, boardId));
    }
}
