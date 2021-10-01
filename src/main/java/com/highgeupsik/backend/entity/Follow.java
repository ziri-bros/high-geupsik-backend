package com.highgeupsik.backend.entity;


import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Follow extends TimeEntity {

    @GeneratedValue
    @Id
    @Column(name = "follow_id")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "from_user_id", updatable = false)
    private User fromUser; //요청한 사람

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "to_user_id", updatable = false)
    private User toUser; //요청받은 사람

    private String fromUserName;

    private boolean acceptFlag;

    @Builder
    public Follow(User fromUser, User toUser,
                  String fromUserName, boolean acceptFlag) {
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.fromUserName = fromUserName;
        this.acceptFlag = acceptFlag;
    }

    public void acceptFollow() {
        this.acceptFlag = true;
    }

}
