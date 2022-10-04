package com.highgeupsik.backend.api;

import static com.highgeupsik.backend.api.ApiUtils.*;

import com.highgeupsik.backend.service.UserTokenService;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.highgeupsik.backend.dto.TokenDTO;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class AuthController {

    private final UserTokenService userTokenService;

    @ApiOperation(value = "토큰 재발급", notes = "새로운 엑세스 토큰을 리턴 받습니다")
    @PatchMapping("/login/tokens")
    public ApiResult<TokenDTO> tokenDetails(@RequestBody TokenDTO tokenDTO) {
        return success(userTokenService.getTokenDTO(tokenDTO));
    }
}
