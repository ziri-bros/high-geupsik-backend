package com.highgeupsik.backend.api;

import static com.highgeupsik.backend.utils.ApiUtils.*;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.highgeupsik.backend.dto.CommentResDTO;
import com.highgeupsik.backend.resolver.LoginUser;
import com.highgeupsik.backend.service.BoardCommentService;
import com.highgeupsik.backend.utils.ApiResult;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class BoardCommentController {

    private final BoardCommentService boardCommentService;

    @ApiOperation(value = "게시글의 댓글 목록 조회")
    @GetMapping("/boards/{boardId}/comments")
    public ApiResult<Page<CommentResDTO>> boardComments(
        @LoginUser Long userId,
        @PathVariable Long boardId,
        @RequestParam(value = "page", defaultValue = "1") int pageNum,
        @RequestParam(value = "pageSize", defaultValue = "20") int pageSize
    ) {
        return success(boardCommentService.findCommentsBy(userId, boardId, pageNum, pageSize));
    }
}
