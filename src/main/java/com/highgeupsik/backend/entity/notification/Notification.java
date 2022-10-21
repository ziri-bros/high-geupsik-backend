package com.highgeupsik.backend.entity.notification;

import com.highgeupsik.backend.entity.TimeEntity;
import com.highgeupsik.backend.entity.board.Board;
import com.highgeupsik.backend.entity.board.Comment;
import com.highgeupsik.backend.entity.message.Room;
import com.highgeupsik.backend.entity.user.User;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Notification extends TimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long id;

    private boolean readFlag = false;

    private String content;

    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User receiver;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private Comment comment;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;

    @Builder
    public Notification(User receiver, String content, NotificationType notificationType, Board board,
        Comment comment, Room room) {
        this.content = content;
        this.notificationType = notificationType;
        this.receiver = receiver;
        this.board = board;
        this.comment = comment;
        this.room = room;
    }

    public static Notification of(User receiver, String content, NotificationType kind, Board board, Comment comment) {
        return Notification.builder()
            .content(content)
            .notificationType(kind)
            .receiver(receiver)
            .board(board)
            .comment(comment)
            .build();
    }

    public static Notification ofRoom(User receiver, String content, NotificationType kind, Room room) {
        return Notification.builder()
            .content(content)
            .notificationType(kind)
            .receiver(receiver)
            .room(room)
            .build();
    }

    public void readNotification() {
        readFlag = true;
    }
}
