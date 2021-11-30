package com.highgeupsik.backend.api;

import static com.highgeupsik.backend.utils.ApiUtils.*;
import static org.springframework.http.HttpStatus.*;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.highgeupsik.backend.dto.UserReqDTO;
import com.highgeupsik.backend.resolver.LoginUser;
import com.highgeupsik.backend.service.UserConfirmService;
import com.highgeupsik.backend.service.UserQueryService;
import com.highgeupsik.backend.service.UserService;
import com.highgeupsik.backend.utils.ApiResult;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class UserProfileController {

	private final UserService userService;
	private final UserConfirmService userConfirmService;
	private final UserQueryService userQueryService;

	@ApiOperation(value = "내정보 조회")
	@ResponseStatus(OK)
	@GetMapping("/users") //내정보 조회
	public ApiResult myInfo(@LoginUser Long userId) {
		return success(userQueryService.findById(userId));
	}

	@ApiOperation(value = "내정보 수정")
	@ResponseStatus(OK)
	@PatchMapping("/users")
	public ApiResult editMyInfo(@LoginUser Long userId, @RequestBody UserReqDTO userReqDTO) {
		userService.editUserInfo(userId, userReqDTO.getStudentCardDTO(), userReqDTO.getSchoolDTO());
		userConfirmService.saveUserConfirm(userId);
		return success(null);
	}

	@ApiOperation(value = "학생증 제출")
	@PostMapping("/login/cards")
	public ApiResult sendCard(@LoginUser Long userId, @RequestBody UserReqDTO userReqDTO) {
		userService.updateUserInfo(userId, userReqDTO.getStudentCardDTO(), userReqDTO.getSchoolDTO());
		userConfirmService.saveUserConfirm(userId);
		return success(null);
	}
}
