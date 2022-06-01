package com.highgeupsik.backend.service;

import static com.highgeupsik.backend.utils.ErrorMessage.*;

import com.highgeupsik.backend.entity.Board;
import com.highgeupsik.backend.entity.Message;
import com.highgeupsik.backend.entity.Room;
import com.highgeupsik.backend.entity.User;
import com.highgeupsik.backend.exception.ResourceNotFoundException;
import com.highgeupsik.backend.repository.BoardRepository;
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
    private final BoardRepository boardRepository;

    public Long sendMessage(Long fromUserId, Long toUserId, Long boardId, String content) {
        User fromUser = userRepository.findById(fromUserId)
            .orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND));
        User toUser = userRepository.findById(toUserId)
            .orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND));
        Board board = boardRepository.findById(boardId)
            .orElseThrow(() -> new ResourceNotFoundException(BOARD_NOT_FOUND));

        Room fromUserRoom = roomRepository.findByBoardAndFromUser(board, fromUser)
            .orElse(Room.of(fromUser, toUser, board));
        Room toUserRoom = roomRepository.findByBoardAndFromUser(board, toUser)
            .orElse(Room.of(toUser, fromUser, board));

        Message fromMessage = Message.of(fromUser, toUser, fromUser, content);
        Message toMessage = Message.of(fromUser, toUser, toUser, content);
        fromUserRoom.addMessage(fromMessage);
        toUserRoom.addMessage(toMessage);

        Long roomId = roomRepository.save(fromUserRoom).getId();
        roomRepository.save(toUserRoom);
        return roomId;
    }

    public void removeRoom(Long userId, Long roomId) {
        Room room = roomRepository.findById(roomId)
            .orElseThrow(() -> new ResourceNotFoundException(ROOM_NOT_FOUND));
        room.checkUser(userId);
        roomRepository.delete(room);
    }
}
