package com.highgeupsik.backend.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Message extends TimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "message_id")
    private Long id;

    private String content;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "from_user_id")
    private User fromUser;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "to_user_id")
    private User toUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;

    private boolean fromDeleteFlag = false;

    private boolean toDeleteFlag = false;

    @Builder
    public Message(String content, User fromUser, User toUser, Room room) {
        this.content = content;
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.room = room;
    }

    public void deleteFromUser() {
        fromDeleteFlag = true;
    }

    public void deleteToUser() {
        toDeleteFlag = true;
    }
}
