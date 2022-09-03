package com.highgeupsik.backend.repository;

import com.highgeupsik.backend.entity.Message;
import com.highgeupsik.backend.entity.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MessageRepository extends JpaRepository<Message, Long>, MessageRepositoryCustom {

    Page<Message> findAllByRoomIdAndOwnerId(Long roomId, Long ownerId, Pageable pageable);

    @Modifying(clearAutomatically = true)
    @Query(value = "update Message m set m.isReceiverRead = true where m.room = :room")
    void updateReadFlagByRoom(@Param("room") Room room);
}
