package com.highgeupsik.backend.api;

import static com.highgeupsik.backend.utils.ApiUtils.*;
import static org.springframework.http.HttpStatus.*;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.highgeupsik.backend.dto.UserConfirmDTO;
import com.highgeupsik.backend.service.UserConfirmService;
import com.highgeupsik.backend.service.UserService;
import com.highgeupsik.backend.utils.ApiResult;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/admin")
@RestController
public class AdminApiController {

    private final UserService userService;
    private final UserConfirmService userConfirmService;

    @ApiOperation(value = "학생증 검수를 위한 유저 조회", notes = "관리자가 유저를 조회합니다")
    @GetMapping("/users")
    public ApiResult<Page<UserConfirmDTO>> userConfirms(
        @RequestParam(value = "page", defaultValue = "0") Integer pageNum) {
        return success(userConfirmService.findAll(pageNum));
    }

    @ApiOperation(value = "학생증 허가")
    @ResponseStatus(OK)
    @PatchMapping("/users/{userId}/authorize") //수락
    public void acceptUser(@PathVariable Long userId) {
        userService.acceptUser(userId);
    }

    @ApiOperation(value = "학생증 거부")
    @ResponseStatus(OK)
    @PatchMapping("/users/{userId}") //거부
    public void rejectUser(@PathVariable Long userId) {
        userService.rejectUser(userId);
    }
}
