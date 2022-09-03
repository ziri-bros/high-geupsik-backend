package com.highgeupsik.backend.repository;

import static com.highgeupsik.backend.utils.PagingUtils.*;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class RoomRepositoryTest extends RepositoryTest {

    @Test
    void findByBoardAndSender() {
        assertThat(roomRepository.findByBoardAndSender(board, sender)).isPresent();
        assertThat(roomRepository.findByBoardAndSender(board, receiver)).isNotEmpty();
    }

    @Test
    void findAllSenderId() {
        assertThat(roomRepository.findAllBySenderId(sender.getId(), orderByModifiedDate(1, 1))
            .getContent())
            .isNotEmpty();
        assertThat(roomRepository.findAllBySenderId(receiver.getId(), orderByModifiedDate(1, 1))
            .getContent())
            .isNotEmpty();
    }
}
