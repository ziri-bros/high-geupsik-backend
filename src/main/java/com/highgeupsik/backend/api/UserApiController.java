package com.highgeupsik.backend.api;


import com.highgeupsik.backend.dto.SchoolInfoDTO;
import com.highgeupsik.backend.dto.SubjectScheduleDTO;
import com.highgeupsik.backend.dto.TokenDTO;
import com.highgeupsik.backend.dto.UserCardReqDTO;
import com.highgeupsik.backend.entity.UploadFile;
import com.highgeupsik.backend.resolver.LoginUser;
import com.highgeupsik.backend.service.*;
import com.highgeupsik.backend.utils.ApiResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

import static com.highgeupsik.backend.utils.ApiUtils.*;

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
    public ApiResult sendCard(@LoginUser Long userId, UserCardReqDTO userCardReqDTO) throws IOException {
        List<UploadFile> cardList = s3Service.uploadFiles(userCardReqDTO.getCardImages());
        return success(userCardService.saveUserCard(userId, cardList));
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


    /***
     * 친구 API
     */
//    @GetMapping("/users/followers") //받은 친구요청 조회
//    public ApiResult<List<FollowDTO>> followers(@AuthenticationPrincipal User user,
//                                                @RequestParam(value = "page", defaultValue = "1") Integer pageNum) {
//        return success(followQueryService.findReceivedFollowRequests(user.getId(), pageNum));
//    }
//
//    @GetMapping("/users/followings")
//    public ApiResult<List<FollowDTO>> followings(@AuthenticationPrincipal User user,
//                                                 @RequestParam(value = "page", defaultValue = "1") Integer pageNum) {
//        return success(followQueryService.findSentFollowRequests(user.getId(), pageNum));
//    }
//
//    @PostMapping("/users/{toUserId}") //친구요청 보내기
//    public ApiResult sendFollow(@AuthenticationPrincipal User user, @PathVariable Long toUserId) {
//        Long fromUserId = user.getId();
//        followService.checkDuplicateFollow(fromUserId, toUserId);
//        followService.saveFollow(fromUserId, toUserId, user.getUsername(), false);
//        return success(toUserId);
//    }
//
//    @DeleteMapping("/users/{fromUserId}") //친구요청 거절
//    public ApiResult rejectFollow(@AuthenticationPrincipal User user,
//                                  @PathVariable Long fromUserId) {
//        followService.deleteFollowRequest(user.getId(), fromUserId);
//        return success(fromUserId);
//    }
//
//    @PatchMapping("/users/{fromUserId}") //친구요청 수락
//    public ApiResult acceptFollow(@AuthenticationPrincipal User user, @PathVariable Long fromUserId) {
//        followService.acceptFollow(fromUserId, user.getId(), user.getUsername());
//        return success(fromUserId);
//    }
//
//    @GetMapping("/users/follows") //친구목록
//    public ApiResult<List<FollowDTO>> follows(@AuthenticationPrincipal User user, @RequestParam(value = "page", defaultValue = "1") Integer pageNum) {
//        return success(followQueryService.findFollows(user.getId(), pageNum));
//    }
//
//    @DeleteMapping("/users/follows/{followId}")//친구삭제
//    public ApiResult deleteFollow(@AuthenticationPrincipal User user, @PathVariable Long followId) {
//        followService.deleteFriend(followId);
//        return success(null);
//    }

    /***
     * 이메일 인증 API
     */
//    @PatchMapping("/join/token") // 이메일 인증 클릭
//    public ApiResult confirmEmail(@RequestParam(value = "token") String emailTokenId) {
//        ConfirmToken confirmToken = confirmTokenService.findOneByIdNotExpired(emailTokenId);
//        userService.verifyEmailToken(confirmToken);
//        return success(confirmToken.getId());
//    }
//
//    @PostMapping("/join/token") //이메일 인증링크 다시보내기
//    public ApiResult sendToken(@AuthenticationPrincipal User user) throws MessagingException {
//        String tokenId = confirmTokenService.findByUserId(user.getId());
//        confirmTokenService.deletePrevToken(tokenId);
//        String newTokenId = confirmTokenService.saveConfirmToken(user.getId());
//        mailService.sendEmailToken(newTokenId, user.getEmail(), user.getUsername());
//        return success(newTokenId);
//    }

}
