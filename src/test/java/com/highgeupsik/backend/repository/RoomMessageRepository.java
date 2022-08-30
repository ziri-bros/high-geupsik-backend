package com.highgeupsik.backend.repository;

import com.highgeupsik.backend.entity.Board;
import com.highgeupsik.backend.entity.Message;
import com.highgeupsik.backend.entity.Room;
import com.highgeupsik.backend.entity.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@TestInstance(value = Lifecycle.PER_CLASS)
@DataJpaTest
public class RoomMessageRepository {

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
    Message message;

    @BeforeAll
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
        Message saveMessage = Message.ofOwner(sender, receiver, sender, "content");
        saveMessage.setRoom(room);
        message = messageRepository.save(saveMessage);
    }
}
