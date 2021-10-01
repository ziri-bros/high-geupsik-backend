package com.highgeupsik.backend.service;


import com.highgeupsik.backend.entity.Message;
import com.highgeupsik.backend.repository.MessageRepository;
import com.highgeupsik.backend.repository.RoomRepository;
import com.highgeupsik.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service
@Transactional
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;

    public Long saveMessage(String content, Long fromUserId, Long toUserId, Long roomId) {
        return messageRepository.save(Message.builder()
                .content(content)
                .fromUser(userRepository.findById(fromUserId).get())
                .toUser(userRepository.findById(toUserId).get())
                .room(roomRepository.findById(roomId).get())
                .build()).getId();
    }

    public void deleteMessage(Long messageId, Long userId) {
        Message message = messageRepository.findById(messageId).get();
        Long fromUserId = message.getFromUser().getId();
        if (fromUserId.equals(userId)) { //보낸메세지 삭제요청
            message.deleteFromUser();
        } else { //받은메세지에서 삭제요청
            message.deleteToUser();
        }
        if (message.isFromDeleteFlag() && message.isToDeleteFlag())
            messageRepository.delete(message);
    }



}
