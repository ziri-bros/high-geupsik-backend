package com.highgeupsik.backend.repository;

import com.highgeupsik.backend.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {

    Page<Message> findAllByRoomId(Long roomId, Pageable pageable);
}
