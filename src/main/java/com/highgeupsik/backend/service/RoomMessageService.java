package com.highgeupsik.backend.service;

import static com.highgeupsik.backend.utils.ErrorMessage.*;

import com.highgeupsik.backend.dto.OnlyIdDTO;
import com.highgeupsik.backend.entity.Board;
import com.highgeupsik.backend.entity.Message;
import com.highgeupsik.backend.entity.Room;
import com.highgeupsik.backend.entity.User;
import com.highgeupsik.backend.exception.ResourceNotFoundException;
import com.highgeupsik.backend.repository.BoardRepository;
import com.highgeupsik.backend.repository.MessageRepository;
import com.highgeupsik.backend.repository.RoomRepository;
import com.highgeupsik.backend.repository.UserRepository;
import com.highgeupsik.backend.utils.ApiResult;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class RoomMessageService {

    private final RoomRepository roomRepository;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final NotificationService notificationService;

    //TODO: receiver room 에만 안읽은 count 증가
    public Long sendMessage(Long senderId, Long receiverId, Long boardId, String content) {
        User sender = userRepository.findById(senderId)
            .orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND));
        User receiver = userRepository.findById(receiverId)
            .orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND));
        Board board = boardRepository.findById(boardId)
            .orElseThrow(() -> new ResourceNotFoundException(BOARD_NOT_FOUND));

        Room senderRoom = findOrCreateRoom(board, sender, receiver);
        Room receiverRoom = findOrCreateRoom(board, receiver, sender);

        //TODO: factory method 분리
        senderRoom.addMessage(Message.ofOwner(sender, receiver, sender, content, false));
        receiverRoom.addMessage(Message.ofOwner(sender, receiver, receiver, content, true));
        receiverRoom.addNewMessageCount();
        roomRepository.save(senderRoom);
        roomRepository.save(receiverRoom);

        notificationService.saveRoomNotification(receiver, receiverRoom);

        return senderRoom.getId();
    }

    private Room findOrCreateRoom(Board board, User sender, User receiver) {
        return roomRepository.findByBoardAndSender(board, sender)
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
        return roomRepository.findByBoardAndSender(boardRepository.findById(room.getBoard().getId())
                    .orElseThrow(() -> new ResourceNotFoundException(BOARD_NOT_FOUND)),
                userRepository.findById(room.getReceiver().getId())
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