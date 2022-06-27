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

    public RoomDTO(Room room) {
        roomId = room.getId();
        boardId = room.getBoard().getId();
        receiverId = room.getReceiver().getId();
        recentMessage = room.getRecentMessage();
    }
}
