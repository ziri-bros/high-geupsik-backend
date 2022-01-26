package com.highgeupsik.backend.api;

import static com.highgeupsik.backend.utils.ApiUtils.*;

import javax.mail.MessagingException;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.highgeupsik.backend.dto.UserConfirmDTO;
import com.highgeupsik.backend.dto.UserResDTO;
import com.highgeupsik.backend.service.MailService;
import com.highgeupsik.backend.service.UserConfirmService;
import com.highgeupsik.backend.service.UserQueryService;
import com.highgeupsik.backend.service.UserService;
import com.highgeupsik.backend.utils.ApiResult;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/admin")
@RestController
public class AdminApiController {

    private final UserService userService;
    private final UserQueryService userQueryService;
    private final UserConfirmService userConfirmService;
    private final MailService mailService;

    @ApiOperation(value = "학생증 검수를 위한 유저 조회", notes = "관리자가 유저를 조회합니다")
    @GetMapping("/users")
    public ApiResult<Page<UserConfirmDTO>> userCards(
        @RequestParam(value = "page", defaultValue = "1") Integer pageNum) {
        return success(userConfirmService.findAll(pageNum));
    }

    @ApiOperation(value = "학생증 허가")
    @PatchMapping("/users/{userId}/authorize") //수락
    public ApiResult acceptCard(@PathVariable("userId") Long userId) throws MessagingException {
        UserResDTO userResDTO = userQueryService.findById(userId);
        userService.updateRoleUser(userId);
        userConfirmService.deleteUserConfirmByUserId(userId);
        mailService.sendEmail(userResDTO.getUsername(), userResDTO.getEmail(), true);
        return success(null);
    }

    @ApiOperation(value = "학생증 거부")
    @PatchMapping("/users/{userId}") //거부
    public ApiResult denyCard(@PathVariable("userId") Long userId) throws MessagingException {
        UserResDTO user = userQueryService.findById(userId);
        userConfirmService.deleteUserConfirmByUserId(userId);
        mailService.sendEmail(user.getUsername(), user.getEmail(), false);
        return success(null);
    }
}
