package com.highgeupsik.backend.entity;

import static org.assertj.core.api.Assertions.assertThat;

import com.highgeupsik.backend.entity.message.Message;
import com.highgeupsik.backend.entity.message.Room;
import com.highgeupsik.backend.entity.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MessageTest {

	User sender;
	User receiver;
	String content;

	@BeforeEach
	void init() {
		sender = User.ofUser();
		receiver = User.ofUser();
		content = "content";
	}

	@Test
	void create() {
		Message message = Message.send(sender, receiver, content);
		Message message2 = Message.receive(receiver, sender, content);

		assertThat(message.getContent()).isEqualTo(content);
		assertThat(message.getOwner()).isEqualTo(sender);
		assertThat(message2.getOwner()).isEqualTo(receiver);
	}

	@Test
	void updateRoom() {
		Message message = Message.send(sender, receiver, content);
		Room room = Room.of();
		message.setRoom(room);

		assertThat(message.getRoom()).isEqualTo(room);
		assertThat(message.getRoom()).isNotNull();
	}
}