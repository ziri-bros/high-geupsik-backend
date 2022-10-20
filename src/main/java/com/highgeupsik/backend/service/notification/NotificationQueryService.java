package com.highgeupsik.backend.service.notification;

import static com.highgeupsik.backend.utils.PagingUtils.*;

import com.highgeupsik.backend.api.notification.NotificationDTO;
import com.highgeupsik.backend.repository.NotificationRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class NotificationQueryService {

    private final NotificationRepository notificationRepository;
    private static final int NOTIFICATION_COUNT = 10;

    public NotificationResult findAllByUserId(Long userId, Integer pageNum) {
        Page<NotificationDTO> notifications = notificationRepository.findAllByReceiverId(userId,
            orderByCreatedDateDESC(pageNum, NOTIFICATION_COUNT))
            .map(NotificationDTO::new);
        int remainNotificationCount = getRemainNotificationCount(notifications);
        return new NotificationResult(remainNotificationCount, notifications);
    }

    private int getRemainNotificationCount(Page<NotificationDTO> notifications) {
        int count = 0;
        for (NotificationDTO notification : notifications) {
            if(!notification.isReadFlag()){
                count++;
            }
        }
        return count;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class NotificationResult {
        int remainNotificationCount;
        Page<NotificationDTO> notifications;
    }

}
