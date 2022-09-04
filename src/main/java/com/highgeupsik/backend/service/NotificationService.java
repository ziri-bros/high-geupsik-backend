package com.highgeupsik.backend.service;

import static com.highgeupsik.backend.utils.ErrorMessage.NOTIFICATION_NOT_FOUND;
import static com.highgeupsik.backend.utils.NotificationMessage.COMMENT_NOTIFICATION;
import static com.highgeupsik.backend.utils.NotificationMessage.MESSAGE_NOTIFICATION;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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

@RequiredArgsConstructor
@Transactional
@Service
public class NotificationService {

	private final NotificationRepository notificationRepository;
	private final ApplicationEventPublisher applicationEventPublisher;

	@Transactional(propagation = Propagation.MANDATORY)
	public void saveCommentNotification(User user, Board board, Comment comment, NotificationType type) {
		Notification notification = Notification.of(user, comment.getContent(), type, board, comment);
		notificationRepository.save(notification);
		applicationEventPublisher.publishEvent(new AlarmEvent(user.getId(), COMMENT_NOTIFICATION));
	}

	@Transactional(propagation = Propagation.MANDATORY)
	public void saveRoomNotification(User user, Room room) {
		Notification notification = Notification.ofRoom(user, room.getRecentMessage(), NotificationType.MESSAGE, room);
		notificationRepository.save(notification);
		applicationEventPublisher.publishEvent(new AlarmEvent(user.getId(), MESSAGE_NOTIFICATION));
	}

	public void readNotification(Long notificationId) {
		Notification notification = notificationRepository.findById(notificationId)
			.orElseThrow(() -> new ResourceNotFoundException(NOTIFICATION_NOT_FOUND));
		notification.readNotification();
	}

	public void deleteByComment(Comment comment) {
		notificationRepository.deleteByComment(comment);
		notificationRepository.deleteAllByCommentIn(comment.getChildren());
	}
}
