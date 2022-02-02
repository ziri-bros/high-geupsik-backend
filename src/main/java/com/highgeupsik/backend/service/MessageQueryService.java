package com.highgeupsik.backend.service;

import com.highgeupsik.backend.dto.MessageResDTO;
import com.highgeupsik.backend.repository.MessageRepository;
import com.highgeupsik.backend.utils.PagingUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MessageQueryService {

    private final MessageRepository messageRepository;
    private final static int MESSAGE_COUNT = 10;

    public Page<MessageResDTO> findAll(Long roomId, Integer pageNum) {
        return messageRepository.findAllByRoomId(roomId, PagingUtils.orderByCreatedDateDESC(
            pageNum, MESSAGE_COUNT)).map(MessageResDTO::new);
    }
}
