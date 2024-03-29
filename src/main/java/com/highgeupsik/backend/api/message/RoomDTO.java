package com.highgeupsik.backend.api.message;

import com.highgeupsik.backend.entity.message.Room;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@NoArgsConstructor
@Getter
public class RoomDTO {

    private Long roomId;
    private Long boardId;
    private Long receiverId;
    private String recentMessage;
    private int newMessageCount;
    private LocalDateTime modifiedDate;
    //TODO: 마지막으로 온 대화의 시간이 몇시인지 + 읽지 않은 메시지 개수

    public RoomDTO(Room room) {
        roomId = room.getId();
        boardId = room.getBoard().getId();
        receiverId = room.getReceiver().getId();
        newMessageCount = room.getNewMessageCount();
        recentMessage = room.getRecentMessage();
        modifiedDate = room.getModifiedDate();
    }
}
