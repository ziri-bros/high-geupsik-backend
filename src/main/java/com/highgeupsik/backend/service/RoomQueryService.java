package com.highgeupsik.backend.service;

import static com.highgeupsik.backend.utils.PagingUtils.*;

import com.highgeupsik.backend.dto.RoomDTO;
import com.highgeupsik.backend.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class RoomQueryService {

    private final RoomRepository roomRepository;
    private static final int ROOM_COUNT = 10;

    public Page<RoomDTO> findAllByFromUserId(Long fromUserId, Integer pageNum) {
        return roomRepository.findByFromUserId(fromUserId, orderByModifiedDate(pageNum, ROOM_COUNT))
            .map(RoomDTO::new);
    }
}
