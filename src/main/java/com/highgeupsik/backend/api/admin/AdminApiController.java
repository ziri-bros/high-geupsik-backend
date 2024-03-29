package com.highgeupsik.backend.api.admin;

import static com.highgeupsik.backend.api.ApiUtils.success;
import static com.highgeupsik.backend.utils.PagingUtils.DEFAULT_PAGE_NUMBER;
import static org.springframework.http.HttpStatus.OK;

import com.highgeupsik.backend.api.ApiResult;
import com.highgeupsik.backend.service.user.UserAdminService;
import com.highgeupsik.backend.service.user.UserConfirmService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/admin")
@RestController
public class AdminApiController {

    private final UserAdminService userAdminService;
    private final UserConfirmService userConfirmService;

    @ApiOperation(value = "학생증 검수를 위한 유저 조회", notes = "관리자가 유저를 조회합니다")
    @GetMapping("/users")
    public ApiResult<Page<UserConfirmDTO>> userConfirms(
        @RequestParam(value = "page", defaultValue = DEFAULT_PAGE_NUMBER) Integer pageNum) {
        return success(userConfirmService.findAll(pageNum));
    }

    @ApiOperation(value = "학생증 허가")
    @ResponseStatus(OK)
    @PatchMapping("/users/{userId}/authorize") //수락
    public void acceptUser(@PathVariable Long userId) {
        userAdminService.acceptUser(userId);
    }

    @ApiOperation(value = "학생증 거부")
    @ResponseStatus(OK)
    @PatchMapping("/users/{userId}") //거부
    public void rejectUser(@PathVariable Long userId) {
        userAdminService.rejectUser(userId);
    }
}
