package com.highgeupsik.backend.api;

import static com.highgeupsik.backend.utils.ApiUtils.*;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.highgeupsik.backend.dto.CommentReqDTO;
import com.highgeupsik.backend.resolver.LoginUser;
import com.highgeupsik.backend.service.BoardQueryService;
import com.highgeupsik.backend.service.CommentQueryService;
import com.highgeupsik.backend.service.CommentService;
import com.highgeupsik.backend.utils.ApiResult;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class CommentController {

	private final CommentService commentService;
	private final CommentQueryService commentQueryService;
	private final BoardQueryService boardQueryService;

	@ApiOperation(value = "댓글 작성")
	@PostMapping("/boards/{boardId}/comments")
	public ApiResult writeComment(
		@LoginUser Long userId,
		@PathVariable("boardId") Long boardId,
		@RequestBody CommentReqDTO commentReqDTO
	) {
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
			return success(commentService.saveReplyComment(userId, commentReqDTO.getContent(), parentId, boardId, -1));
		}  //글 작성자 아닐때
		int userCount = commentQueryService.findUserCountByUserIdAndBoardId(userId, boardId);
		if (userCount == 0) { //처음 작성
			return success(commentService.saveReplyComment(userId, commentReqDTO.getContent(), parentId, boardId));
		}
		return success(
			commentService.saveReplyComment(userId, commentReqDTO.getContent(), parentId, boardId, userCount));
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
}
