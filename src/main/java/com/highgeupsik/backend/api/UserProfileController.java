package com.highgeupsik.backend.api;

import static com.highgeupsik.backend.utils.ApiUtils.*;
import static org.springframework.http.HttpStatus.*;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.highgeupsik.backend.dto.UserReqDTO;
import com.highgeupsik.backend.resolver.LoginUser;
import com.highgeupsik.backend.service.UserQueryService;
import com.highgeupsik.backend.service.UserService;
import com.highgeupsik.backend.utils.ApiResult;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/users")
@RestController
public class UserProfileController {

    private final UserService userService;
    private final UserQueryService userQueryService;

    @ApiOperation(value = "내정보 조회")
    @ResponseStatus(OK)
    @GetMapping
    public ApiResult userDetails(@LoginUser Long userId) {
        return success(userQueryService.findById(userId));
    }

    @ApiOperation(value = "내정보 수정")
    @ResponseStatus(OK)
    @PatchMapping
    public void userModify(@LoginUser Long userId, @RequestBody UserReqDTO userReqDTO) {
        userService.modifyUser(userId, userReqDTO.getStudentCardDTO(), userReqDTO.getSchoolReqDTO());
    }
}
