package com.highgeupsik.backend.service;

import static com.highgeupsik.backend.utils.ErrorMessage.USER_NOT_FOUND;
import static com.highgeupsik.backend.utils.PagingUtils.*;

import com.highgeupsik.backend.dto.RoomDTO;
import com.highgeupsik.backend.exception.ResourceNotFoundException;
import com.highgeupsik.backend.repository.RoomRepository;
import com.highgeupsik.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class RoomQueryService {

    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private static final int ROOM_COUNT = 10;

    public Page<RoomDTO> findAll(Long userId, Integer pageNum) {
        return roomRepository.findAllByFromUser(
            userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND)),
            orderByModifiedDate(pageNum, ROOM_COUNT))
            .map(RoomDTO::new);
    }
}
