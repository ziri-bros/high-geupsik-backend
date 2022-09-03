package com.highgeupsik.backend.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.highgeupsik.backend.entity.Room;
import com.highgeupsik.backend.repository.BoardRepository;
import com.highgeupsik.backend.repository.RoomRepository;
import com.highgeupsik.backend.repository.UserRepository;

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
		given(roomRepository.findById(anyLong())).willReturn(Optional.of(Room.builder().build()));
		given(roomRepository.findByBoardAndSender(any(), any())).willReturn(Optional.of(Room.builder().build()));
		//when
		roomMessageService.sendMessage(1L, "content");
		//then
		verify(notificationService, times(1)).saveRoomNotification(any(), any());
	}
}