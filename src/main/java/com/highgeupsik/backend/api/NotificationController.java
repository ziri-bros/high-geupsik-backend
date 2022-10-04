package com.highgeupsik.backend.api;

import static com.highgeupsik.backend.api.ApiUtils.success;
import static com.highgeupsik.backend.utils.PagingUtils.DEFAULT_PAGE_NUMBER;

import com.highgeupsik.backend.resolver.LoginUser;
import com.highgeupsik.backend.service.NotificationQueryService;
import com.highgeupsik.backend.service.NotificationQueryService.NotificationResult;
import com.highgeupsik.backend.service.NotificationService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class NotificationController {

    private final NotificationService notificationService;
    private final NotificationQueryService notificationQueryService;

    @GetMapping("/notifications")
    public ApiResult<NotificationResult> notifications(@LoginUser Long userId,
        @RequestParam(value = "page", defaultValue = DEFAULT_PAGE_NUMBER) Integer pageNum) {
        return success(notificationQueryService.findAllByUserId(userId, pageNum));
    }

    @PutMapping("/notifications/{notificationId}")
    public void readNotification(@PathVariable Long notificationId) {
        notificationService.readNotification(notificationId);
    }
}
