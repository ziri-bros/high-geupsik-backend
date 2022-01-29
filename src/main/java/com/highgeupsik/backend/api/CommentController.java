package com.highgeupsik.backend.api;

import static com.highgeupsik.backend.utils.ApiUtils.*;
import static org.springframework.http.HttpStatus.*;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.highgeupsik.backend.dto.CommentReqDTO;
import com.highgeupsik.backend.dto.CommentResDTO;
import com.highgeupsik.backend.resolver.LoginUser;
import com.highgeupsik.backend.service.CommentService;
import com.highgeupsik.backend.utils.ApiResult;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class CommentController {

    private final CommentService commentService;

    @ApiOperation(value = "댓글 작성")
    @PostMapping("/boards/{boardId}/comments")
    public ApiResult<CommentResDTO> commentSave(
        @LoginUser Long userId,
        @PathVariable("boardId") Long boardId,
        @RequestBody CommentReqDTO dto
    ) {
        return success(commentService.saveComment(userId, boardId, dto));
    }

    @ApiOperation(value = "댓글 편집")
    @PutMapping("/comments/{commentId}")
    public ApiResult commentModify(@PathVariable("commentId") Long commentId, @RequestBody CommentReqDTO commentReqDTO,
        @LoginUser Long userId) {
        return success(commentService.modifyComment(userId, commentId, commentReqDTO));
    }

    @ApiOperation(value = "댓글 삭제")
    @ResponseStatus(NO_CONTENT)
    @DeleteMapping("/comments/{commentId}")
    public void commentRemove(@PathVariable("commentId") Long commentId, @LoginUser Long userId) {
        commentService.removeComment(userId, commentId);
    }
}
