package com.highgeupsik.backend.dto;

import com.highgeupsik.backend.entity.Room;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class RoomResDTO {

    private Long id;
    private Long fromUserId;
    private Long toUserId;
    private String firstMessage;

    public RoomResDTO(Room room){
        id = room.getId();
        fromUserId = room.getFromUser().getId();
        toUserId = room.getToUser().getId();
        firstMessage = room.getFirstMessage();
    }

}
