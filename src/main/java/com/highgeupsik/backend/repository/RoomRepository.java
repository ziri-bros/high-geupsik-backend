package com.highgeupsik.backend.repository;

import com.highgeupsik.backend.entity.Room;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long> {

    public Optional<Room> findOneByFromUserIdAndToUserId(Long fromUserId, Long toUserId);

    public Page<Room> findByFromUserId(Long fromUserId, Pageable pageable);

}
