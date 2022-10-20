package com.highgeupsik.backend.events;

import com.highgeupsik.backend.service.alarm.AlarmService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@RequiredArgsConstructor
@Component
public class EventHandler {

    private final AlarmService alarmService;

    @Async
    @TransactionalEventListener
    public void handleAlarmEvent(AlarmEvent alarmEvent) {
        alarmService.send(alarmEvent.getUserId(), alarmEvent.getMessage());
    }
}
