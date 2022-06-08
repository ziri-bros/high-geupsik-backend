package com.highgeupsik.backend.service;

import static com.highgeupsik.backend.utils.NotificationMessage.*;
import static com.highgeupsik.backend.utils.PagingUtils.*;

import com.highgeupsik.backend.dto.NotificationResDTO;
import com.highgeupsik.backend.entity.Notification;
import com.highgeupsik.backend.entity.NotificationType;
import com.highgeupsik.backend.repository.NotificationRepository;
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

    public Page<NotificationResDTO> findAllByUserId(Long userId, Integer pageNum) {
        return notificationRepository.findAllByReceiverId(userId, orderByCreatedDateDESC(pageNum, NOTIFICATION_COUNT))
            .map((notification) -> new NotificationResDTO(
                notification.getId(),
                getTargetId(notification),
                notification.isReadFlag(),
                getContent(notification),
                notification.getNotificationKind()));
    }

    private String getContent(Notification notification) {
        NotificationType notificationKind = notification.getNotificationKind();
        if (notificationKind.equals(NotificationType.COMMENT)) {
            return COMMENT_NOTIFICATION;
        }
        if (notificationKind.equals(NotificationType.REPLY)) {
            return REPLY_NOTIFICATION;
        }
        return MESSAGE_NOTIFICATION;
    }

    public Long getTargetId(Notification notification) {
        NotificationType notificationKind = notification.getNotificationKind();
        if (notificationKind.equals(NotificationType.COMMENT)) {
            return notification.getBoard().getId();
        }
        if (notificationKind.equals(NotificationType.REPLY)) {
            return notification.getComment().getId();
        }
        return notification.getRoom().getId();
    }
}
