package com.highgeupsik.backend.service;

import static com.highgeupsik.backend.utils.PagingUtils.orderByModifiedDate;

import com.highgeupsik.backend.dto.RoomResDTO;
import com.highgeupsik.backend.entity.Room;
import com.highgeupsik.backend.repository.RoomRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class RoomQueryService {

    private final RoomRepository roomRepository;
    private static final int ROOM_COUNT = 10;

    public RoomResDTO findOneByUserId(Long fromUserId, Long toUserId) {
        Optional<Room> room = roomRepository.findOneByFromUserIdAndToUserId(fromUserId, toUserId);
        if (room.isPresent()) {
            return new RoomResDTO(room.get());
        }
        return null;
    }

    public List<RoomResDTO> findRooms(Long userId, Integer pageNum) {
        return roomRepository.findByFromUserId(userId, orderByModifiedDate(pageNum, ROOM_COUNT)).getContent()
            .stream().map((room) -> new RoomResDTO(room)).collect(Collectors.toList());
    }
}
