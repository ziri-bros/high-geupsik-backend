package com.highgeupsik.backend.repository.message;

import static com.highgeupsik.backend.entity.message.QMessage.*;
import static com.highgeupsik.backend.entity.message.QRoom.*;

import com.highgeupsik.backend.entity.message.Message;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MessageRepositoryImpl implements MessageRepositoryCustom {

    private final JPAQueryFactory query;

    @Override
    public List<Message> findAllByRoomIdAndIdLessThan(Long roomId, Long lastMessageId, int MESSAGE_COUNT) {
        return query.selectFrom(message)
            .join(message.room, room)
            .where(roomIdEq(roomId), lastMessageIdLt(lastMessageId))
            .orderBy(message.id.desc())
            .limit(MESSAGE_COUNT)
            .fetch();
    }

    private BooleanExpression roomIdEq(Long roomId) {
        return message.room.id.eq(roomId);
    }

    private BooleanExpression lastMessageIdLt(Long lastMessageId) {
        return Objects.nonNull(lastMessageId) ? message.id.lt(lastMessageId) : null;
    }
}
