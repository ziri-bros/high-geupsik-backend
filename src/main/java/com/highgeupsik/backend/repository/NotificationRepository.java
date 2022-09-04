package com.highgeupsik.backend.repository;

import com.highgeupsik.backend.entity.Room;
import java.util.List;

import com.highgeupsik.backend.entity.Comment;
import com.highgeupsik.backend.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    Page<Notification> findAllByReceiverId(Long receiverId, Pageable pageable);

    void deleteByComment(Comment comment);

    void deleteAllByCommentIn(List<Comment> comments);

    void deleteAllByRoom(Room room);
}
