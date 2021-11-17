package com.highgeupsik.backend.api;


import static com.highgeupsik.backend.utils.ApiUtils.success;

import com.highgeupsik.backend.dto.SchoolInfoDTO;
import com.highgeupsik.backend.dto.SubjectScheduleDTO;
import com.highgeupsik.backend.dto.TokenDTO;
import com.highgeupsik.backend.dto.UserCardReqDTO;
import com.highgeupsik.backend.entity.UploadFile;
import com.highgeupsik.backend.resolver.LoginUser;
import com.highgeupsik.backend.service.S3Service;
import com.highgeupsik.backend.service.SubjectScheduleQueryService;
import com.highgeupsik.backend.service.SubjectScheduleService;
import com.highgeupsik.backend.service.UserCardQueryService;
import com.highgeupsik.backend.service.UserCardService;
import com.highgeupsik.backend.service.UserQueryService;
import com.highgeupsik.backend.service.UserService;
import com.highgeupsik.backend.utils.ApiResult;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserApiController {

    private final S3Service s3Service;
    private final UserService userService;
    private final UserQueryService userQueryService;
    private final UserCardService userCardService;
    private final UserCardQueryService userCardQueryService;
    private final SubjectScheduleService subjectScheduleService;
    private final SubjectScheduleQueryService subjectScheduleQueryService;


    @GetMapping("/login/cards") //학생증 조회
    public ApiResult cards(@LoginUser Long userId) {
        return success(userCardQueryService.findUserIdByCardId(userId));
    }

    @PostMapping("/login/cards") //학생증 제출
    public ApiResult sendCard(@LoginUser Long userId, @RequestBody UserCardReqDTO userCardReqDTO) {
        return success(userCardService.saveUserCard(userId, userCardReqDTO.getUploadFileDTO()));
    }

    @DeleteMapping("/login/cards") //학생증 제출 취소
    public ApiResult deleteCard(@LoginUser Long userId) {
        userCardService.deleteUserCardByUserId(userId);
        return success(null);
    }

    @PostMapping("/login/schoolInfo") //학교정보 제출
    public ApiResult<TokenDTO> sendSchoolInfo(@LoginUser Long userId, @RequestBody SchoolInfoDTO schoolInfoDTO) {
        userService.updateSchoolInfo(userId, schoolInfoDTO);
        return success(userService.login(userId));
    }

    @PutMapping("/login/schoolInfo") //학교정보 수정
    public ApiResult<TokenDTO> editSchoolInfo(@LoginUser Long userId, @RequestBody SchoolInfoDTO schoolInfoDTO) {
        userService.updateSchoolInfo(userId, schoolInfoDTO);
        return success(null);
    }

    @GetMapping("/login/token") //로그인
    public ApiResult<TokenDTO> login(@RequestBody TokenDTO tokenDTO) {
        return success(userService.updateToken(tokenDTO));
    }

    @GetMapping("/users") //내정보 조회
    public ApiResult myInfo(@LoginUser Long userId) {
        return success(userQueryService.findById(userId));
    }

    /***
     * 시간표 API
     */
    @GetMapping("/users/schedule")
    public ApiResult<SubjectScheduleDTO> schedule(@LoginUser Long userId) {
        return success(subjectScheduleQueryService.findSubjectSchedule(userId));
    }

    @PostMapping("/users/schedule")
    public ApiResult makeSchedule(@LoginUser Long userId,
        @RequestBody SubjectScheduleDTO subjectScheduleDTO) {
        return success(subjectScheduleService.makeSubjectSchedule(subjectScheduleDTO, userId));
    }

    @DeleteMapping("/users/schedule")
    public ApiResult deleteSchedule(@LoginUser Long userId) {
        subjectScheduleService.deleteSchedule(userId);
        return success(null);
    }

}
