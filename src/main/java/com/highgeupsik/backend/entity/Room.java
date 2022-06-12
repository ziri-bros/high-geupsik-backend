package com.highgeupsik.backend.entity;

import static com.highgeupsik.backend.utils.ErrorMessage.*;

import com.highgeupsik.backend.exception.UserException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Room extends TimeEntity {

    @Id
    @Column(name = "room_id")
    @GeneratedValue
    private Long id;

    private String latestMessage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_user_id")
    private User fromUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_user_id")
    private User toUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @OneToOne(mappedBy = "room", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Notification notification;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    private List<Message> messageList = new ArrayList<>();

    @Builder
    public Room(User fromUser, User toUser, Board board) {
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.board = board;
    }

    public static Room of(User fromUser, User toUser, Board board) {
        return Room.builder()
            .fromUser(fromUser)
            .toUser(toUser)
            .board(board)
            .build();
    }

    public void addMessage(Message message) {
        messageList.add(message);
        message.setRoom(this);
        setLatestMessage(message.getContent());
    }

    public void setLatestMessage(String latestMessage) {
        this.latestMessage = latestMessage;
    }

    public void checkUser(Long userId){
        if(!isRoomUser(userId)){
            throw new UserException(NOT_ROOM_USER);
        }
    }

    public boolean isRoomUser(Long userId) {
        if(fromUser.getId().equals(userId)){
            return true;
        }
        return false;
    }

    public void setNotification(Notification notification){
        this.notification = notification;
    }
}
