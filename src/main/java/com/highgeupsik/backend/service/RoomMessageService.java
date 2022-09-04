package com.highgeupsik.backend.service;

import static com.highgeupsik.backend.utils.ErrorMessage.BOARD_NOT_FOUND;
import static com.highgeupsik.backend.utils.ErrorMessage.ROOM_NOT_FOUND;
import static com.highgeupsik.backend.utils.ErrorMessage.USER_NOT_FOUND;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.highgeupsik.backend.dto.OnlyIdDTO;
import com.highgeupsik.backend.entity.Board;
import com.highgeupsik.backend.entity.Room;
import com.highgeupsik.backend.entity.User;
import com.highgeupsik.backend.exception.ResourceNotFoundException;
import com.highgeupsik.backend.repository.BoardRepository;
import com.highgeupsik.backend.repository.MessageRepository;
import com.highgeupsik.backend.repository.RoomRepository;
import com.highgeupsik.backend.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional
@Service
public class RoomMessageService {

    private final RoomRepository roomRepository;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final NotificationService notificationService;

    public Long sendMessage(Long roomId, String content) {
        Room senderRoom = roomRepository.findById(roomId)
            .orElseThrow(() -> new ResourceNotFoundException(ROOM_NOT_FOUND));
        Room receiverRoom = roomRepository.findByBoardAndSender(senderRoom.getBoard(), senderRoom.getReceiver())
            .orElseThrow(() -> new ResourceNotFoundException(ROOM_NOT_FOUND));

        senderRoom.sendMessage(content);
        receiverRoom.receiveMessage(content);

        notificationService.saveRoomNotification(senderRoom.getReceiver(), receiverRoom);

        return senderRoom.getId();
    }

    private Room findOrCreateRoom(Board board, User sender, User receiver) {
        return roomRepository.findByBoardAndSenderAndReceiver(board, sender, receiver)
            .orElse(Room.of(sender, receiver, board));
    }

    public void deleteRoom(Long userId, Long roomId) {
        Room room = roomRepository.findById(roomId)
            .orElseThrow(() -> new ResourceNotFoundException(ROOM_NOT_FOUND));
        room.checkUser(userId);
        roomRepository.delete(room);
    }

    public void readNewMessagesByRoomId(Long roomId) {
        Room myRoom = roomRepository.findById(roomId)
            .orElseThrow(() -> new ResourceNotFoundException(ROOM_NOT_FOUND));
        myRoom.readNewMessages();
        Room otherRoom = findOtherRoom(myRoom);
        messageRepository.updateReadFlagByRoom(otherRoom);
    }

    private Room findOtherRoom(Room room) {
        return roomRepository.findByBoardAndSenderAndReceiver(boardRepository.findById(room.getBoard().getId())
                    .orElseThrow(() -> new ResourceNotFoundException(BOARD_NOT_FOUND)),
                userRepository.findById(room.getReceiver().getId())
                    .orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND)),
                userRepository.findById(room.getSender().getId())
                    .orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND)))
            .orElseThrow(() -> new ResourceNotFoundException(ROOM_NOT_FOUND));
    }

    public OnlyIdDTO createRoom(Long userId, Long receiverId, Long boardId) {
        User sender = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND));
        User receiver = userRepository.findById(receiverId)
            .orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND));
        Board board = boardRepository.findById(boardId)
            .orElseThrow(() -> new ResourceNotFoundException(BOARD_NOT_FOUND));

        Room senderRoom = findOrCreateRoom(board, sender, receiver);
        Room receiverRoom = findOrCreateRoom(board, receiver, sender);

        roomRepository.save(senderRoom);
        roomRepository.save(receiverRoom);

        return new OnlyIdDTO(senderRoom.getId());
    }
}