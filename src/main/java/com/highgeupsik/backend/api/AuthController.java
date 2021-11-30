package com.highgeupsik.backend.api;

import static com.highgeupsik.backend.utils.ApiUtils.*;
import static org.springframework.http.HttpStatus.*;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.highgeupsik.backend.dto.TokenDTO;
import com.highgeupsik.backend.service.UserService;
import com.highgeupsik.backend.utils.ApiResult;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class AuthController {

	private final UserService userService;

	@ApiOperation(value = "로그인", notes = "새로운 토큰을 리턴 받습니다")
	@ResponseStatus(OK)
	@GetMapping("/login/token")
	public ApiResult<TokenDTO> login(@RequestBody TokenDTO tokenDTO) {
		return success(userService.updateToken(tokenDTO));
	}
}
