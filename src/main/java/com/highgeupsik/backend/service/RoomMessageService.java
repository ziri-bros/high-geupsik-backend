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
    private final NotificationService notificationService;

    public Long sendMessage(Long senderId, Long receiverId, Long boardId, String content) {
        User sender = userRepository.findById(senderId)
            .orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND));
        User receiver = userRepository.findById(receiverId)
            .orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND));
        Board board = boardRepository.findById(boardId)
            .orElseThrow(() -> new ResourceNotFoundException(BOARD_NOT_FOUND));

        Room senderRoom = roomRepository.findByBoardAndSender(board, sender)
            .orElse(Room.of(sender, receiver, board));
        Room receiverRoom = roomRepository.findByBoardAndSender(board, receiver)
            .orElse(Room.of(receiver, sender, board));

        Message messageOfSender = Message.of(sender, receiver, sender, content);
        Message messageOfReceiver = Message.of(sender, receiver, receiver, content);
        senderRoom.addMessage(messageOfSender);
        receiverRoom.addMessage(messageOfReceiver);

        roomRepository.save(senderRoom);
        roomRepository.save(receiverRoom);

        notificationService.saveRoomNotification(receiver, receiverRoom);
        return senderRoom.getId();
    }

    public void deleteRoom(Long userId, Long roomId) {
        Room room = roomRepository.findById(roomId)
            .orElseThrow(() -> new ResourceNotFoundException(ROOM_NOT_FOUND));
        room.checkUser(userId);
        roomRepository.delete(room);
    }
}
