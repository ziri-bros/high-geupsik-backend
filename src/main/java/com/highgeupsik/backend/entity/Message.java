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

    //TODO: 읽었는지 안읽었는지 FLAG 추가

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    private User sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id")
    private User receiver;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private User owner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;

    @Builder
    public Message(User sender, User receiver, User owner, String content) {
        this.content = content;
        this.sender = sender;
        this.receiver = receiver;
        this.owner = owner;
    }

    //TODO: FLAG
    public static Message ofOwner(User sender, User receiver, User owner, String content) {
        return Message.builder()
            .sender(sender)
            .receiver(receiver)
            .owner(owner)
            .content(content)
            .build();
    }

    public void setRoom(Room room) {
        this.room = room;
    }
}
