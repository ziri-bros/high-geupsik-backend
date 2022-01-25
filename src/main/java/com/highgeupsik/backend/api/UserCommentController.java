package com.highgeupsik.backend.api;

import static com.highgeupsik.backend.utils.ApiUtils.*;

import com.highgeupsik.backend.dto.CommentResDTO;
import com.highgeupsik.backend.resolver.LoginUser;
import com.highgeupsik.backend.service.CommentQueryService;
import com.highgeupsik.backend.utils.ApiResult;
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
    public ApiResult<Page<CommentResDTO>> myComments(@RequestParam(value = "page", defaultValue = "1") Integer pageNum,
        @LoginUser Long userId) {
        return success(commentQueryService.findByMyId(userId, pageNum));
    }
}
