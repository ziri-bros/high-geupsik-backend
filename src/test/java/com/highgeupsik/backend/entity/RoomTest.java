package com.highgeupsik.backend.entity;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RoomTest {

    User sender;
    User receiver;
    String content;
    Board board;

    @BeforeEach
    void init() {
        sender = new User();
        receiver = new User();
        board = new Board();
        content = "content";
    }

    @Test
    void create() {
        Room room = Room.of(sender, receiver, board);

        assertThat(room.getSender()).isEqualTo(sender);
        assertThat(room.getReceiver()).isEqualTo(receiver);
        assertThat(room.getBoard()).isEqualTo(board);
    }

    @Test
    void setRecentMessage() {
        Room room = Room.of(sender, receiver, board);

        assertThat(room.getRecentMessage()).isNull();

        room.setRecentMessage(content);

        assertThat(room.getRecentMessage()).isEqualTo(content);
    }

    @Test
    void addMessage() {
        Room room = Room.of(sender, receiver, board);

        assertThat(room.getMessageList()).isEmpty();

        Message message = new Message(sender, receiver, sender, content);
        room.addMessage(message);

        assertThat(room.getMessageList()).containsExactly(message);
        assertThat(message.getRoom()).isEqualTo(room);
    }
}