package com.highgeupsik.backend.dto;

import com.highgeupsik.backend.entity.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class NotificationDTO {

    private Long notificationId;
    private Long targetId;
    private boolean readFLag;
    private String content;
    private NotificationType notificationKind;
}
