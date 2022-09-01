package com.highgeupsik.backend.dto;

import com.highgeupsik.backend.entity.Message;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class MessageResDTO {

    private Long messageId;
    private String content;
    private Long senderId;
    private Long receiverId;
    //TODO: 메시지 보내진 시간 + 내가 보낸 메시지를 상대가 읽었는지

    public MessageResDTO(Message message) {
        messageId = message.getId();
        content = message.getContent();
        senderId = message.getSender().getId();
        receiverId = message.getReceiver().getId();
    }
}
