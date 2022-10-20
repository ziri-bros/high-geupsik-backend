package com.highgeupsik.backend.api.notification;

import static com.highgeupsik.backend.entity.notification.NotificationType.*;

import com.highgeupsik.backend.entity.notification.Notification;
import com.highgeupsik.backend.entity.notification.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class NotificationDTO {

    private Long notificationId;
    private Long boardId;
    private Long commentId;
    private Long roomId;
    private boolean readFlag;
    private String content;
    private NotificationType notificationType;

    public NotificationDTO(Notification notification) {
        notificationId = notification.getId();
        boardId = notification.getNotificationType() == MESSAGE ? null : notification.getBoard().getId();
        commentId = notification.getNotificationType() == REPLY ? notification.getComment().getId() : null;
        roomId = notification.getNotificationType() == MESSAGE ? notification.getRoom().getId() : null;
        readFlag = notification.isReadFlag();
        content = notification.getContent();
        notificationType = notification.getNotificationType();
    }
}
