package com.highgeupsik.backend.service;

import static com.highgeupsik.backend.utils.ErrorMessage.*;

import com.highgeupsik.backend.entity.Message;
import com.highgeupsik.backend.entity.Room;
import com.highgeupsik.backend.entity.User;
import com.highgeupsik.backend.exception.NotFoundException;
import com.highgeupsik.backend.repository.RoomRepository;
import com.highgeupsik.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class RoomMessageService {

    private final RoomRepository roomRepository;
    private final UserRepository userRepository;

    public void sendMessage(Long fromUserId, Long toUserId, String content) {
        User fromUser = userRepository.findById(fromUserId)
            .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));
        User toUser = userRepository.findById(toUserId)
            .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));

        Room myRoom = roomRepository.findByFromUserAndToUser(fromUser, toUser)
            .orElse(Room.of(fromUser, toUser));
        Room otherRoom = roomRepository.findByFromUserAndToUser(toUser, fromUser)
            .orElse(Room.of(toUser, fromUser));

        Message myMessage = Message.of(fromUser, toUser, myRoom, content);
        Message otherMessage = Message.of(fromUser, toUser, otherRoom, content);

        myRoom.addMessage(myMessage);
        otherRoom.addMessage(otherMessage);

        myRoom.setLatestMessage(content);
        otherRoom.setLatestMessage(content);

        roomRepository.save(myRoom);
        roomRepository.save(otherRoom);
    }

    public void removeRoom(Long roomId) {
        roomRepository.deleteById(roomId);
    }
}
