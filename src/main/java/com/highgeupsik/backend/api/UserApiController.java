package com.highgeupsik.backend.api;


import static com.highgeupsik.backend.utils.ApiUtils.success;

import com.highgeupsik.backend.dto.SubjectScheduleDTO;
import com.highgeupsik.backend.dto.TokenDTO;
import com.highgeupsik.backend.dto.UserCardReqDTO;
import com.highgeupsik.backend.resolver.LoginUser;
import com.highgeupsik.backend.service.SubjectScheduleQueryService;
import com.highgeupsik.backend.service.SubjectScheduleService;
import com.highgeupsik.backend.service.UserCardQueryService;
import com.highgeupsik.backend.service.UserCardService;
import com.highgeupsik.backend.service.UserQueryService;
import com.highgeupsik.backend.service.UserService;
import com.highgeupsik.backend.utils.ApiResult;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserApiController {

    private final UserService userService;
    private final UserCardService userCardService;
    private final UserCardQueryService userCardQueryService;
    private final SubjectScheduleService subjectScheduleService;
    private final SubjectScheduleQueryService subjectScheduleQueryService;

    @ApiOperation(value = "학생증 제출")
    @PostMapping("/login/cards")
    public ApiResult sendCard(@LoginUser Long userId, @RequestBody UserCardReqDTO userCardReqDTO) {
        return success(userCardService.saveUserCard(userId, userCardReqDTO.getThumbnail(),
            userCardReqDTO.getSchoolDTO()));
    }

    @ApiOperation(value = "학생증 제출 취소")
    @DeleteMapping("/login/cards")
    public ApiResult deleteCard(@LoginUser Long userId) {
        userCardService.deleteUserCardByUserId(userId);
        return success(null);
    }

    @ApiOperation(value = "로그인", notes = "새로운 토큰을 리턴 받습니다")
    @GetMapping("/login/token") //로그인
    public ApiResult<TokenDTO> login(@RequestBody TokenDTO tokenDTO) {
        return success(userService.updateToken(tokenDTO));
    }

    @ApiOperation(value = "내정보 조회")
    @GetMapping("/users") //내정보 조회
    public ApiResult myInfo(@LoginUser Long userId) {
        return success(userCardQueryService.findByUserId(userId));
    }

    /***
     * 시간표 API
     */
    @ApiOperation(value = "시간표 조회")
    @GetMapping("/users/schedule")
    public ApiResult<SubjectScheduleDTO> schedule(@LoginUser Long userId) {
        return success(subjectScheduleQueryService.findSubjectSchedule(userId));
    }

    @ApiOperation(value = "시간표 제출")
    @PostMapping("/users/schedule")
    public ApiResult makeSchedule(@LoginUser Long userId,
        @RequestBody SubjectScheduleDTO subjectScheduleDTO) {
        return success(subjectScheduleService.makeSubjectSchedule(subjectScheduleDTO, userId));
    }

    @ApiOperation(value = "시간표 삭제")
    @DeleteMapping("/users/schedule")
    public ApiResult deleteSchedule(@LoginUser Long userId) {
        subjectScheduleService.deleteSchedule(userId);
        return success(null);
    }

}
