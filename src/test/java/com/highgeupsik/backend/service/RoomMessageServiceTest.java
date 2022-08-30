package com.highgeupsik.backend.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.highgeupsik.backend.entity.Board;
import com.highgeupsik.backend.entity.Room;
import com.highgeupsik.backend.entity.User;
import com.highgeupsik.backend.repository.BoardRepository;
import com.highgeupsik.backend.repository.RoomRepository;
import com.highgeupsik.backend.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class RoomMessageServiceTest {

    @InjectMocks
    RoomMessageService roomMessageService;
    @Mock
    RoomRepository roomRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    BoardRepository boardRepository;
    @Mock
    NotificationService notificationService;

    @Test
    void sendMessage() {
        given(userRepository.findById(any(Long.class)))
            .willReturn(Optional.of(User.builder().build()));
        given(boardRepository.findById(any(Long.class)))
            .willReturn(Optional.of(Board.builder().build()));
        given(roomRepository.findByBoardAndSender(any(Board.class), any(User.class)))
            .willReturn(Optional.of(Room.builder().build()));
        //when
        roomMessageService.sendMessage(1L, 1L, 1L, "content");
        //then
        verify(roomRepository, times(2)).save(any(Room.class));
        verify(notificationService, times(1))
            .saveRoomNotification(any(User.class), any(Room.class));
    }
}