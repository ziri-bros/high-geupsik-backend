package com.highgeupsik.backend.service;


import com.highgeupsik.backend.dto.MessageResDTO;
import com.highgeupsik.backend.entity.Message;
import com.highgeupsik.backend.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.highgeupsik.backend.utils.PagingUtils.orderByCreatedDateDESC;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MessageQueryService {

    private final MessageRepository messageRepository;

    private static final int MESSAGE_COUNT = 5;

    public Message findOneById(Long messageId) {
        return messageRepository.findById(messageId).get();
    }

    public List<MessageResDTO> findMessages(Long roomId, Integer pageNum) {
        List<Message> messages = messageRepository.findByRoomId(roomId, orderByCreatedDateDESC(pageNum, MESSAGE_COUNT)).getContent();
        return messages.stream().map((message) -> new MessageResDTO(message)).collect(Collectors.toList());
    }

}
