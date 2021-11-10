package com.highgeupsik.backend.api;


import static com.highgeupsik.backend.utils.ApiUtils.success;

import com.highgeupsik.backend.dto.UserCardResDTO;
import com.highgeupsik.backend.dto.UserResDTO;
import com.highgeupsik.backend.service.MailService;
import com.highgeupsik.backend.service.UserCardQueryService;
import com.highgeupsik.backend.service.UserCardService;
import com.highgeupsik.backend.service.UserQueryService;
import com.highgeupsik.backend.service.UserService;
import com.highgeupsik.backend.utils.ApiResult;
import javax.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RequestMapping("/admin")
@RestController
@RequiredArgsConstructor
public class UserCardApiController {

    private final UserService userService;
    private final UserQueryService userQueryService;
    private final UserCardService userCardService;
    private final UserCardQueryService userCardQueryService;
    private final MailService mailService;

    @GetMapping("/cards")
    public ApiResult<Page<UserCardResDTO>> userCards(
        @RequestParam(value = "page", defaultValue = "1") Integer pageNum) {
        return success(userCardQueryService.findUserCards(pageNum));
    }

    @PatchMapping("/cards/{cardId}") //수락
    public ApiResult acceptCard(@PathVariable("cardId") Long cardId) throws MessagingException {
        Long userId = userCardQueryService.findUserIdByCardId(cardId);
        UserResDTO user = userQueryService.findById(userId);
        userService.updateRole(userId);
        userCardService.deleteUserCard(cardId);
        mailService.sendEmail(user.getUsername(), user.getEmail(), true);
        return success(null);
    }

    @DeleteMapping("/cards/{cardId}") //거부
    public ApiResult denyCard(@PathVariable("cardId") Long cardId) throws MessagingException {
        UserResDTO user = userQueryService.findById(userCardQueryService.findUserIdByCardId(cardId));
        userCardService.deleteUserCard(cardId);
        mailService.sendEmail(user.getUsername(), user.getEmail(), false);
        return success(null);
    }

}
