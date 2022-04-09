package com.highgeupsik.backend.api;

import static com.highgeupsik.backend.utils.ApiUtils.*;
import static org.springframework.http.HttpStatus.*;

import javax.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.highgeupsik.backend.dto.BoardReqDTO;
import com.highgeupsik.backend.dto.BoardResDTO;
import com.highgeupsik.backend.dto.BoardSearchCondition;
import com.highgeupsik.backend.resolver.LoginUser;
import com.highgeupsik.backend.service.BoardQueryService;
import com.highgeupsik.backend.service.BoardService;
import com.highgeupsik.backend.utils.ApiResult;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/boards")
@RestController
public class BoardController {

    private final BoardService boardService;
    private final BoardQueryService boardQueryService;

    @ApiOperation(value = "게시글 단일 조회")
    @GetMapping("/{boardId}")
    public ApiResult<BoardResDTO> boardDetails(@LoginUser Long userId, @PathVariable Long boardId) {
        return success(boardQueryService.findOneById(userId, boardId));
    }

    @ApiOperation(value = "게시글 목록 조회")
    @GetMapping
    public ApiResult<Page<BoardResDTO>> boards(@RequestParam(value = "page", defaultValue = "1") Integer pageNum,
        @Valid BoardSearchCondition condition) {
        return success(boardQueryService.findAll(pageNum, condition));
    }

    @ApiOperation(value = "게시글 작성")
    @PostMapping
    public ApiResult boardSave(@LoginUser Long userId, @RequestBody BoardReqDTO boardReqDTO) {
        return success(boardService.makeBoard(userId, boardReqDTO));
    }

    @ApiOperation(value = "게시글 편집")
    @PutMapping("/{boardId}")
    public ApiResult boardModify(@LoginUser Long userId, @PathVariable Long boardId,
        @RequestBody BoardReqDTO boardReqDTO) {
        return success(boardService.modifyBoard(userId, boardId, boardReqDTO));
    }

    @ApiOperation(value = "게시글 삭제")
    @ResponseStatus(NO_CONTENT)
    @DeleteMapping("/{boardId}")
    public void boardRemove(@LoginUser Long userId, @PathVariable Long boardId) {
        boardService.removeBoard(userId, boardId);
    }
}
