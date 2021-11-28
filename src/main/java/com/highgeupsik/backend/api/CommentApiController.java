package com.highgeupsik.backend.api;


import static com.highgeupsik.backend.utils.ApiUtils.success;

import com.highgeupsik.backend.dto.CommentReqDTO;
import com.highgeupsik.backend.dto.CommentResDTO;
import com.highgeupsik.backend.resolver.LoginUser;
import com.highgeupsik.backend.service.BoardQueryService;
import com.highgeupsik.backend.service.CommentQueryService;
import com.highgeupsik.backend.service.CommentService;
import com.highgeupsik.backend.service.LikeService;
import com.highgeupsik.backend.utils.ApiResult;
import com.highgeupsik.backend.utils.ApiUtils;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CommentApiController {

    private final LikeService likeService;
    private final CommentService commentService;
    private final CommentQueryService commentQueryService;
    private final BoardQueryService boardQueryService;

    @ApiOperation(value = "댓글 작성")
    @PostMapping("/boards/{boardId}/comments")
    public ApiResult writeComment(@PathVariable("boardId") Long boardId, @RequestBody CommentReqDTO commentReqDTO,
        @LoginUser Long userId) {
        Long postWriterId = boardQueryService.findWriterIdByBoardId(boardId);
        if (postWriterId.equals(userId)) { //작성자가 댓글쓸때
            return success(commentService.saveComment(userId, commentReqDTO.getContent(), boardId, -1));
        }
        // 글 작성자 아닐때
        int userCount = commentQueryService.findUserCountByUserIdAndBoardId(userId, boardId);
        if (userCount == 0) { //처음 작성
            return success(commentService.saveComment(userId, commentReqDTO.getContent(), boardId));
        }
        return success(commentService.saveComment(userId, commentReqDTO.getContent(), boardId, userCount));
    }

    @ApiOperation(value = "대댓글 작성")
    @PostMapping("/boards/{boardId}/comments/{parentId}")
    public ApiResult writeReplyComment(@PathVariable("boardId") Long boardId, @PathVariable("parentId") Long parentId,
        @RequestBody CommentReqDTO commentReqDTO, @LoginUser Long userId) {
        Long postWriterId = boardQueryService.findWriterIdByBoardId(boardId);
        if (postWriterId.equals(userId)) { //작성자가 댓글쓸때
            return success(commentService.saveReplyComment(userId, commentReqDTO.getContent(),
                parentId, boardId, -1));
        }  //글 작성자 아닐때
        int userCount = commentQueryService.findUserCountByUserIdAndBoardId(userId, boardId);
        if (userCount == 0) { //처음 작성
            return success(commentService.saveReplyComment(userId, commentReqDTO.getContent()
                , parentId, boardId));
        }
        return success(commentService.saveReplyComment(userId, commentReqDTO.getContent()
            , parentId, boardId, userCount));
    }


    @ApiOperation(value = "댓글 목록 조회")
    @GetMapping("/boards/{boardId}/comments")
    public ApiResult<List<CommentResDTO>> comments(@PathVariable("boardId") Long boardId,
        @RequestParam(value = "page", defaultValue = "1") Integer pageNum) {
        return success(commentQueryService.findByBoardId(boardId, pageNum));
    }

    @ApiOperation(value = "댓글 편집", notes = "댓글 편집 화면으로 넘어가기 위해 댓글 정보를 리턴")
    @PutMapping("/comments/{commentId}")
    public ApiResult editComment(@PathVariable("commentId") Long commentId, @RequestBody CommentReqDTO commentReqDTO,
        @LoginUser Long userId) {
        return success(commentService.updateComment(userId, commentId, commentReqDTO));
    }

    @ApiOperation(value = "댓글 삭제")
    @DeleteMapping("/boards/{boardId}/comments/{commentId}")
    public ApiResult deleteComment(@PathVariable("boardId") Long boardId, @PathVariable("commentId") Long commentId,
        @LoginUser Long userId) {
        commentService.deleteComment(userId, commentId, boardId);
        return success(null);
    }

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
