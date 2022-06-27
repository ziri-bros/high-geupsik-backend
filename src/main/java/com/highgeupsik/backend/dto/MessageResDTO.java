package com.highgeupsik.backend.dto;

import com.highgeupsik.backend.entity.Message;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class MessageResDTO {

    private Long id;
    private String content;
    private Long senderId;
    private Long receiverId;

    public MessageResDTO(Message message) {
        id = message.getId();
        content = message.getContent();
        senderId = message.getSender().getId();
        receiverId = message.getReceiver().getId();
    }
}
