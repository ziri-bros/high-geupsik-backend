package com.highgeupsik.backend.repository.message;

import com.highgeupsik.backend.entity.message.Message;
import java.util.List;

public interface MessageRepositoryCustom {

    List<Message> findAllByRoomIdAndIdLessThan(Long roomId, Long lastMessageId, int MESSAGE_COUNT);
}
