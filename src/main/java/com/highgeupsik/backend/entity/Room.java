package com.highgeupsik.backend.entity;


import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Room extends TimeEntity{

    @Id
    @Column(name = "room_id")
    @GeneratedValue
    private Long id;

    private String firstMessage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_user_id")
    private User fromUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_user_id")
    private User toUser;

    @Builder
    public Room(User fromUser, User toUser){
        this.fromUser = fromUser;
        this.toUser = toUser;
    }

    public void updateMessage(String firstMessage){
        this.firstMessage = firstMessage;
    }

}
