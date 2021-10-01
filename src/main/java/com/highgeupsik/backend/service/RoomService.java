package com.highgeupsik.backend.service;


import com.highgeupsik.backend.entity.Room;
import com.highgeupsik.backend.exception.NotFoundException;
import com.highgeupsik.backend.repository.RoomRepository;
import com.highgeupsik.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.highgeupsik.backend.utils.ErrorMessage.*;

@Service
@Transactional
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final UserRepository userRepository;

    public Long saveRoom(Long fromUserId, Long toUserId) {
        return roomRepository.save(Room.builder()
                .fromUser(userRepository.findById(fromUserId).get())
                .toUser(userRepository.findById(toUserId).get())
                .build()).getId();
    }

    public void updateMessage(Long roomId, String message) {
        Room room = roomRepository.findById(roomId).get();
        room.updateMessage(message);
    }

    public void deleteRoom(Long roomId) {
        Room room = roomRepository.findById(roomId).orElseThrow(
                () -> new NotFoundException(ROOM_NOT_FOUND));
        roomRepository.delete(room);
    }
}
