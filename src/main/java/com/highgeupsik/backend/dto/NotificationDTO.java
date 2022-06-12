package com.highgeupsik.backend.dto;

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
    private boolean readFLag;
    private String content;
    private NotificationType notificationKind;

    public NotificationDTO(Notification notification) {
        notificationId = notification.getId();
        boardId = notification.getBoard().getId();
        commentId = notification.getComment().getId();
        roomId = notification.getRoom().getId();
        readFLag = notification.isReadFlag();
        content = notification.getContent();
        notificationKind = notification.getNotificationKind();

    }
}
