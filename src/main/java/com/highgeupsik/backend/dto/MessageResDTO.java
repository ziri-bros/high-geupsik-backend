package com.highgeupsik.backend.dto;

import com.highgeupsik.backend.entity.Message;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
public class MessageResDTO {

    private Long id;
    private String content;
    private Long fromUserId;
    private Long toUserId;

    public MessageResDTO(Message message){
        id=message.getId();
        content=message.getContent();
        fromUserId=message.getFromUser().getId();
        toUserId=message.getToUser().getId();
    }
}
