package com.highgeupsik.backend.service;

import com.highgeupsik.backend.dto.MessageResDTO;
import com.highgeupsik.backend.repository.MessageRepository;
import com.highgeupsik.backend.utils.PagingUtils;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MessageQueryService {

    private final MessageRepository messageRepository;

    //TODO: 읽음처리 BULK UPDATE
    public List<MessageResDTO> findAllByRoomIdAndOwnerId(Long roomId, Long lastMessageId) {
        return messageRepository.findAllByRoomIdAndIdLessThan(roomId, lastMessageId, PagingUtils.MESSAGE_COUNT)
            .stream().map((MessageResDTO::new)).collect(Collectors.toList());
    }
}
