package com.highgeupsik.backend.dto;

import static com.highgeupsik.backend.entity.NotificationType.*;

import com.highgeupsik.backend.entity.Category;
import com.highgeupsik.backend.entity.Notification;
import com.highgeupsik.backend.entity.NotificationType;
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
    private NotificationType notificationKind;

    public NotificationDTO(Notification notification) {
        notificationId = notification.getId();
        boardId = notification.getNotificationKind() == MESSAGE ? null : notification.getBoard().getId();
        commentId = notification.getNotificationKind() == REPLY ? notification.getComment().getId() : null;
        roomId = notification.getNotificationKind() == MESSAGE ? notification.getRoom().getId() : null;
        readFlag = notification.isReadFlag();
        content = notification.getContent();
        notificationKind = notification.getNotificationKind();
    }
}
