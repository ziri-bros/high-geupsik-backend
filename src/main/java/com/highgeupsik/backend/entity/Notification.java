package com.highgeupsik.backend.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
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
public class Notification {

    @Id
    @GeneratedValue
    @Column(name = "notification_id")
    private Long id;

    private boolean readFlag = false;

    private String content;

    @Enumerated(EnumType.STRING)
    private NotificationType notificationKind;

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
    public Notification(User receiver, String content, NotificationType notificationKind) {
        this.receiver = receiver;
        this.notificationKind = notificationKind;
        this.content = content;
    }

    public static Notification of(User receiver, String content, NotificationType notificationKind) {
        return Notification.builder()
            .receiver(receiver)
            .content(content)
            .notificationKind(notificationKind)
            .build();
    }

    public void setComment(Comment comment) {
        this.comment = comment;
        comment.setNotification(this);
    }

    public void setRoom(Room room) {
        this.room = room;
        room.setNotification(this);
    }

    public void setBoard(Board board) {
        this.board = board;
        board.setNotification(this);
    }

    public void readNotification() {
        readFlag = true;
    }
}
