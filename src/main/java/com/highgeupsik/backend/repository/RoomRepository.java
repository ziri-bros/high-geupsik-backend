package com.highgeupsik.backend.repository;

import com.highgeupsik.backend.entity.Room;
import com.highgeupsik.backend.entity.User;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long> {

    Optional<Room> findByFromUserAndToUser(User fromUser, User toUser);

    Page<Room> findAllByFromUser(User fromUser, Pageable pageable);
}
