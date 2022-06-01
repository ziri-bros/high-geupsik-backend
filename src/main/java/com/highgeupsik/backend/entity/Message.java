package com.highgeupsik.backend.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
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
public class Message extends TimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "message_id")
    private Long id;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_user_id")
    private User fromUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_user_id")
    private User toUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private User owner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;

    @Builder
    public Message(User fromUser, User toUser, User owner, String content) {
        this.content = content;
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.owner = owner;
    }

    public static Message of(User fromUser, User toUser, User owner, String content) {
        return Message.builder()
            .fromUser(fromUser)
            .toUser(toUser)
            .owner(owner)
            .content(content)
            .build();
    }

    public void setRoom(Room room) {
        this.room = room;
    }
}
