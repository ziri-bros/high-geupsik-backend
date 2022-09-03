package com.highgeupsik.backend.repository;

import com.highgeupsik.backend.entity.Message;
import java.util.List;

public interface MessageRepositoryCustom {

    List<Message> findAllByRoomIdAndIdLessThan(Long roomId, Long lastMessageId, int MESSAGE_COUNT);
}
