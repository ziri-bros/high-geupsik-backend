package com.highgeupsik.backend.api.user;

import static com.highgeupsik.backend.api.ApiUtils.*;
import static com.highgeupsik.backend.utils.PagingUtils.DEFAULT_PAGE_NUMBER;

import com.highgeupsik.backend.api.ApiResult;
import com.highgeupsik.backend.api.comment.CommentResDTO;
import com.highgeupsik.backend.resolver.LoginUser;
import com.highgeupsik.backend.service.comment.CommentQueryService;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class UserCommentController {

    private final CommentQueryService commentQueryService;

    @ApiOperation(value = "내가 작성한 댓글 목록 조회")
    @GetMapping("/comments/my")
    public ApiResult<Page<CommentResDTO>> myComments(@LoginUser Long userId,
        @RequestParam(value = "page", defaultValue = DEFAULT_PAGE_NUMBER) Integer pageNum) {
        return success(commentQueryService.findByMyId(userId, pageNum));
    }
}
