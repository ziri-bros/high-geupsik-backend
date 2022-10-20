package com.highgeupsik.backend.api.message;

import java.time.LocalDateTime;

import com.highgeupsik.backend.entity.message.Message;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class MessageResDTO {

	private Long messageId;
	private String content;
	private Long senderId;
	private Long receiverId;
	private boolean isReceiverRead;
	private LocalDateTime createdDate;

	public MessageResDTO(Message message) {
		messageId = message.getId();
		content = message.getContent();
		senderId = message.getSender().getId();
		receiverId = message.getReceiver().getId();
		isReceiverRead = message.isReceiverRead();
		createdDate = message.getCreatedDate();
	}
}
