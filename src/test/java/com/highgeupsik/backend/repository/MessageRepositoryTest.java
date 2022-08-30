package com.highgeupsik.backend.repository;

import static com.highgeupsik.backend.utils.PagingUtils.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class MessageRepositoryTest extends RoomMessageRepository {

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
}