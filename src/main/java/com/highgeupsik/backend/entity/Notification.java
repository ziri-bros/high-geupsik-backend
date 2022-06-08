package com.highgeupsik.backend.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Notification {

    @Id
    @GeneratedValue
    @Column(name = "notification_id")
    private Long id;

    private boolean readFlag = false;

    @Enumerated(EnumType.STRING)
    private NotificationType notificationKind;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User receiver;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private Comment comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;

    @Builder
    public Notification(User receiver, Board board, Comment comment, Room room, NotificationType notificationKind) {
        this.receiver = receiver;
        this.board = board;
        this.comment = comment;
        this.room = room;
        this.notificationKind = notificationKind;
    }

    public static Notification ofBoard(User receiver, Board board, NotificationType notificationKind) {
        return Notification.builder()
            .receiver(receiver)
            .board(board)
            .notificationKind(notificationKind)
            .build();
    }

    public static Notification ofComment(User receiver, Comment comment, NotificationType notificationKind) {
        return Notification.builder()
            .receiver(receiver)
            .comment(comment)
            .notificationKind(notificationKind)
            .build();
    }

    public static Notification ofRoom(User receiver, Room room, NotificationType notificationKind) {
        return Notification.builder()
            .receiver(receiver)
            .room(room)
            .notificationKind(notificationKind)
            .build();
    }

    public void readNotification() {
        readFlag = true;
    }
}
