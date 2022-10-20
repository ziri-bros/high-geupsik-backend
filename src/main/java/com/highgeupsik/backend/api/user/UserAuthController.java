package com.highgeupsik.backend.api.user;

import static com.highgeupsik.backend.api.ApiUtils.*;

import com.highgeupsik.backend.api.ApiResult;
import com.highgeupsik.backend.service.user.UserTokenService;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class UserAuthController {

    private final UserTokenService userTokenService;

    @ApiOperation(value = "토큰 재발급", notes = "새로운 엑세스 토큰을 리턴 받습니다")
    @PatchMapping("/login/tokens")
    public ApiResult<UserTokenDTO> tokenDetails(@RequestBody UserTokenDTO userTokenDTO) {
        return success(userTokenService.getTokenDTO(userTokenDTO));
    }
}
