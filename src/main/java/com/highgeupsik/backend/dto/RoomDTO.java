package com.highgeupsik.backend.dto;

import com.highgeupsik.backend.entity.Room;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class RoomDTO {

    private Long roomId;
    private Long boardId;
    private Long toUserId;
    private String latestMessage;

    public RoomDTO(Room room) {
        roomId = room.getId();
        boardId = room.getBoard().getId();
        toUserId = room.getToUser().getId();
        latestMessage = room.getLatestMessage();
    }
}
