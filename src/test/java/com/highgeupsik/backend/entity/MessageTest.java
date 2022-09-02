package com.highgeupsik.backend.entity;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MessageTest {

    User sender;
    User receiver;
    String content;

    @BeforeEach
    void init() {
        sender = new User();
        receiver = new User();
        content = "content";
    }

    @Test
    void create() {
        Message message = Message.ofOwner(sender, receiver, sender, content, false);
        Message message2 = Message.ofOwner(sender, receiver, receiver, content, false);

        assertThat(message.getContent()).isEqualTo(content);
        assertThat(message.getOwner()).isEqualTo(sender);
        assertThat(message2.getOwner()).isEqualTo(receiver);
    }

    @Test
    void updateRoom() {
        Message message = Message.ofOwner(sender, receiver, sender, content, false);
        Room room = new Room();
        message.setRoom(room);

        assertThat(message.getRoom()).isEqualTo(room);
        assertThat(message.getRoom()).isNotNull();
    }
}