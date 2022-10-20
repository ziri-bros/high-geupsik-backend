package com.highgeupsik.backend.repository;

import com.highgeupsik.backend.repository.board.BoardRepository;
import com.highgeupsik.backend.repository.message.MessageRepository;
import com.highgeupsik.backend.repository.message.RoomRepository;
import com.highgeupsik.backend.repository.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.highgeupsik.backend.entity.board.Board;
import com.highgeupsik.backend.entity.message.Message;
import com.highgeupsik.backend.entity.message.Room;
import com.highgeupsik.backend.entity.user.User;

@TestInstance(value = Lifecycle.PER_CLASS)
@Transactional
@SpringBootTest
public abstract class RepositoryTest {

	@Autowired
    RoomRepository roomRepository;
	@Autowired
    MessageRepository messageRepository;
	@Autowired
    BoardRepository boardRepository;
	@Autowired
	UserRepository userRepository;

	User sender;
	User receiver;
	Board board;
	Room room;
	Room room2;
	Message message;

	@BeforeEach
	void init() {
		board = boardRepository.save(Board.builder().build());
		sender = userRepository.save(User.builder()
			.email("email")
			.username("username").build());
		receiver = userRepository.save(User.builder()
			.email("email")
			.username("receiver").build());
		room = roomRepository.save(Room.builder()
			.sender(sender)
			.board(board).build());
		room2 = roomRepository.save(Room.builder()
			.sender(receiver)
			.board(board).build());
		Message saveMessage = Message.send(sender, receiver, "content");
		saveMessage.setRoom(room);
		message = messageRepository.save(saveMessage);
	}
}
