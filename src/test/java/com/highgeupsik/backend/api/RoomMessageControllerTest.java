package com.highgeupsik.backend.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.net.URI;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.highgeupsik.backend.api.message.MessageReqDTO;
import com.highgeupsik.backend.api.message.RoomCreateRequest;
import com.highgeupsik.backend.entity.board.Board;
import com.highgeupsik.backend.entity.message.Message;
import com.highgeupsik.backend.entity.user.Role;
import com.highgeupsik.backend.entity.message.Room;
import com.highgeupsik.backend.entity.user.User;
import com.highgeupsik.backend.jwt.JwtTokenProvider;
import com.highgeupsik.backend.repository.board.BoardRepository;
import com.highgeupsik.backend.repository.message.MessageRepository;
import com.highgeupsik.backend.repository.message.RoomRepository;
import com.highgeupsik.backend.repository.user.UserRepository;
import com.highgeupsik.backend.service.message.RoomMessageService;

@TestInstance(value = Lifecycle.PER_CLASS)
@AutoConfigureMockMvc
@SpringBootTest
public class RoomMessageControllerTest {

	@Autowired
	MockMvc mockMvc;
	@Autowired
	RoomMessageService roomMessageService;
	@Autowired
	BoardRepository boardRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	RoomRepository roomRepository;
	@Autowired
	MessageRepository messageRepository;
	@Autowired
	ObjectMapper objectMapper;
	@Autowired
	JwtTokenProvider jwtTokenProvider;
	String token;
	User sender;
	User receiver;
	Board board;
	Room room;
	Room room2;
	Message message;

	@BeforeAll
	void init() {
		sender = userRepository.save(User.builder()
			.email("email")
			.username("sender")
			.role(Role.ROLE_USER).build());
		receiver = userRepository.save(User.builder()
			.email("email")
			.username("receiver").build());
		board = boardRepository.save(Board.builder().build());
		token = "Bearer " + jwtTokenProvider.createAccessToken(sender.getId(), "ROLE_USER");
		room = roomRepository.save(Room.ofBoard(sender, receiver, board));
		room2 = roomRepository.save(Room.ofBoard(receiver, sender, board));
		message = Message.send(sender, receiver, "content");
		message.setRoom(room);
		message = messageRepository.save(message);
	}

	@Test
	void sendMessage() throws Exception {
		MessageReqDTO req = new MessageReqDTO();
		req.setContent("new");
		mockMvc.perform(post(URI.create("/rooms/" + room.getId() + "/messages"))
				.header("Authorization", token)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(req)))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("data").exists());
	}

	@Test
	void rooms() throws Exception {
		mockMvc.perform(get(URI.create("/rooms"))
				.header("Authorization", token))
			.andExpect(status().isOk())
			.andExpect(jsonPath("data").exists())
			.andExpect(jsonPath("data.content[0].roomId").value(room.getId()))
			.andExpect(jsonPath("data.content[0].boardId").value(room.getBoard().getId()))
			.andExpect(jsonPath("data.content[0].receiverId").value(room.getReceiver().getId()));
	}

	@Test
	void messages() throws Exception {
		mockMvc.perform(get(URI.create("/rooms/" + room.getId()))
				.header("Authorization", token))
			.andExpect(status().isOk())
			.andExpect(jsonPath("data").exists())
			.andExpect(jsonPath("data[0].messageId").value(message.getId()))
			.andExpect(jsonPath("data[0].content").value(message.getContent()))
			.andExpect(jsonPath("data[0].senderId").value(message.getSender().getId()))
			.andExpect(jsonPath("data[0].receiverId").value(message.getReceiver().getId()));
	}

	@Test
	void createRoom() throws Exception {
		Board newBoard = boardRepository.save(Board.builder().build());
		RoomCreateRequest req = new RoomCreateRequest(newBoard.getId(), receiver.getId());

		mockMvc.perform(post(URI.create("/rooms"))
				.header("Authorization", token)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(req)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("data.id").exists());
	}

	@Test
	void createRoomReturnsSameRoomWhenAlreadyExist() throws Exception {
		RoomCreateRequest req = new RoomCreateRequest(board.getId(), receiver.getId());

		mockMvc.perform(post(URI.create("/rooms"))
				.header("Authorization", token)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(req)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("data.id").value(room.getId()));
	}

	@Test
	void deleteRoom() throws Exception {
		Room deleteRoom = roomRepository.save(Room.ofBoard(sender, receiver, board));
		mockMvc.perform(delete(URI.create("/rooms/" + deleteRoom.getId()))
				.header("Authorization", token))
			.andExpect(status().isOk());
		mockMvc.perform(delete(URI.create("/rooms/" + deleteRoom.getId()))
				.header("Authorization", token))
			.andExpect(status().isNotFound());
	}
}