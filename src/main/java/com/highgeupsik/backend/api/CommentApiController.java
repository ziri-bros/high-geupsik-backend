package com.highgeupsik.backend.api;


import com.highgeupsik.backend.dto.CommentReqDTO;
import com.highgeupsik.backend.dto.CommentResDTO;
import com.highgeupsik.backend.resolver.LoginUser;
import com.highgeupsik.backend.service.BoardDetailQueryService;
import com.highgeupsik.backend.service.CommentQueryService;
import com.highgeupsik.backend.service.CommentService;
import com.highgeupsik.backend.service.LikeService;
import com.highgeupsik.backend.utils.ApiResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.highgeupsik.backend.utils.ApiUtils.*;

@RestController
@RequiredArgsConstructor
public class CommentApiController {

    private final LikeService likeService;
    private final CommentService commentService;
    private final CommentQueryService commentQueryService;
    private final BoardDetailQueryService boardDetailQueryService;

    @PostMapping("/boards/{boardId}/comments") //댓글 달기
    public ApiResult writeComment(@PathVariable("boardId") Long boardId, @RequestBody CommentReqDTO commentReqDTO,
                                  @LoginUser Long userId) {
        Long postWriterId = boardDetailQueryService.findWriterIdByBoardId(boardId);
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

    @PostMapping("/boards/{boardId}/comments/{parentId}") //대댓글 달기
    public ApiResult writeReplyComment(@PathVariable("boardId") Long boardId, @PathVariable("parentId") Long parentId,
                                       @RequestBody CommentReqDTO commentReqDTO, @LoginUser Long userId) {
        Long postWriterId = boardDetailQueryService.findWriterIdByBoardId(boardId);
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


    @GetMapping("/boards/{boardId}/comments") //댓글 리스트 보기
    public ApiResult<List<CommentResDTO>> comments(@PathVariable("boardId") Long boardId,
                                                   @RequestParam(value = "page", defaultValue = "1") Integer pageNum) {
        return success(commentQueryService.findByBoardId(boardId, pageNum));
    }

    @PutMapping("/comments/{commentId}") //댓글수정
    public ApiResult editComment(@PathVariable("commentId") Long commentId, @RequestBody CommentReqDTO commentReqDTO,
                                 @LoginUser Long userId) {
        return success(commentService.updateComment(userId, commentId, commentReqDTO));
    }

    @DeleteMapping("/boards/{boardId}/comments/{commentId}") //댓글삭제
    public ApiResult deleteComment(@PathVariable("boardId") Long boardId, @PathVariable("commentId") Long commentId,
                                   @LoginUser Long userId) {
        commentService.deleteComment(userId, commentId, boardId);
        return success(null);
    }

    @PostMapping("/comments/{commentId}/like") //댓글 좋아요
    public ApiResult pressCommentLike(@LoginUser Long userId, @PathVariable Long commentId) {
        return success(likeService.saveOrUpdateCommentLike(userId, commentId));
    }
}
