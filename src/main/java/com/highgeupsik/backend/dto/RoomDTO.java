package com.highgeupsik.backend.dto;

import com.highgeupsik.backend.entity.Room;
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
    //TODO: 마지막으로 온 대화의 시간이 몇시인지 + 읽지 않은 메시지 개수

    public RoomDTO(Room room) {
        roomId = room.getId();
        boardId = room.getBoard().getId();
        receiverId = room.getReceiver().getId();
        recentMessage = room.getRecentMessage();
    }
}
