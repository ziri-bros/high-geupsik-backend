package com.highgeupsik.backend.repository;

import static com.highgeupsik.backend.utils.PagingUtils.MESSAGE_COUNT;
import static com.highgeupsik.backend.utils.PagingUtils.orderByCreatedDateDESC;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import com.highgeupsik.backend.entity.Message;

public class MessageRepositoryTest extends RepositoryTest {

	@Test
	void findAllByRoomIdAndOwnerId() {
		assertThat(messageRepository
			.findAllByRoomIdAndOwnerId(room.getId(), sender.getId(), orderByCreatedDateDESC(1, 10))
			.getContent())
			.isNotEmpty();
		assertThat(messageRepository
			.findAllByRoomIdAndOwnerId(room.getId(), receiver.getId(), orderByCreatedDateDESC(1, 10))
			.getContent())
			.isEmpty();
	}

	@Test
	void returnsLast2MessageWhenLastMessageIdIsNull() {
		Long roomId = room.getId();
		Long messageId = null;

		Message message2 = messageRepository.save(Message.send(sender, receiver, "content"));
		Message message3 = messageRepository.save(Message.send(sender, receiver, "content"));

		message2.setRoom(room);
		message3.setRoom(room);

		List<Message> messages = messageRepository.findAllByRoomIdAndIdLessThan(roomId, messageId, MESSAGE_COUNT);

		Assertions.assertThat(messages.size()).isEqualTo(2);
		Assertions.assertThat(messages.get(0).getId()).isEqualTo(message3.getId());
		Assertions.assertThat(messages.get(1).getId()).isEqualTo(message2.getId());
	}

	@Test
	void returns1MessageLessThenMessageId() {
		Long roomId = room.getId();

		Message message2 = messageRepository.save(Message.send(sender, receiver, "content"));
		Message message3 = messageRepository.save(Message.send(sender, receiver, "content"));

		message2.setRoom(room);
		message3.setRoom(room);

		Long messageId = message2.getId();
		List<Message> messages = messageRepository.findAllByRoomIdAndIdLessThan(roomId, messageId, MESSAGE_COUNT);

		Assertions.assertThat(messages.size()).isEqualTo(1);
		Assertions.assertThat(messages.get(0).getId()).isEqualTo(message.getId());
	}
}