package com.highgeupsik.backend.service;

import static com.highgeupsik.backend.utils.ErrorMessage.NOTIFICATION_NOT_FOUND;
import static com.highgeupsik.backend.utils.NotificationMessage.*;

import com.highgeupsik.backend.entity.Board;
import com.highgeupsik.backend.entity.Comment;
import com.highgeupsik.backend.entity.Notification;
import com.highgeupsik.backend.entity.NotificationType;
import com.highgeupsik.backend.entity.Room;
import com.highgeupsik.backend.entity.User;
import com.highgeupsik.backend.events.AlarmEvent;
import com.highgeupsik.backend.exception.ResourceNotFoundException;
import com.highgeupsik.backend.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Transactional(propagation = Propagation.MANDATORY)
    public void saveCommentNotification(User user, Board board) {
        Notification notification = Notification.of(user, board.getTitle(), NotificationType.COMMENT);
        notification.setBoard(board);
        notificationRepository.save(notification);
        applicationEventPublisher.publishEvent(new AlarmEvent(user.getId(), COMMENT_NOTIFICATION));
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void saveReplyNotification(User user, Board board, Comment comment) {
        Notification notification = Notification.of(user, comment.getContent(), NotificationType.REPLY);
        notification.setComment(comment);
        notification.setBoard(board);
        notificationRepository.save(notification);
        applicationEventPublisher.publishEvent(new AlarmEvent(user.getId(), REPLY_NOTIFICATION));
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void saveRoomNotification(User user, Room room) {
        Notification notification = Notification.of(user, room.getLatestMessage(), NotificationType.MESSAGE);
        notification.setRoom(room);
        notificationRepository.save(notification);
        applicationEventPublisher.publishEvent(new AlarmEvent(user.getId(), MESSAGE_NOTIFICATION));
    }

    public void readNotification(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
            .orElseThrow(() -> new ResourceNotFoundException(NOTIFICATION_NOT_FOUND));
        notification.readNotification();
    }
}
