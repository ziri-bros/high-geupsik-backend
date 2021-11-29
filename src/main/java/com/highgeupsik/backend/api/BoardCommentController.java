package com.highgeupsik.backend.api;

import static com.highgeupsik.backend.utils.ApiUtils.*;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.highgeupsik.backend.dto.CommentResDTO;
import com.highgeupsik.backend.service.CommentQueryService;
import com.highgeupsik.backend.utils.ApiResult;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class BoardCommentController {

	private final CommentQueryService commentQueryService;

	@ApiOperation(value = "댓글 목록 조회")
	@GetMapping("/boards/{boardId}/comments")
	public ApiResult<List<CommentResDTO>> comments(@PathVariable("boardId") Long boardId,
		@RequestParam(value = "page", defaultValue = "1") Integer pageNum) {
		return success(commentQueryService.findByBoardId(boardId, pageNum));
	}
}
