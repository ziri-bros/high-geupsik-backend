package com.highgeupsik.backend.repository;

import com.highgeupsik.backend.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {

    public Page<Message> findByRoomId(Long roomId, Pageable pageable);

    public Page<Message> findByFromUserIdAndFromDeleteFlag(Long fromUserId,
                                                                   boolean fromDeleteFlag,
                                                                   Pageable pageable);

    public Page<Message> findByToUserIdAndToDeleteFlag(Long toUserId,
                                                               boolean toDeleteFlag,
                                                               Pageable pageable);
}
