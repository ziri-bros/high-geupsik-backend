package com.highgeupsik.backend.service.alarm;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface AlarmService {

    Long TIME_OUT = 1000L * 60 * 30;

    SseEmitter subscribe(Long userId);

    void sendToClient(SseEmitter emitter, String id, Object data);

    void send(Long userId, Object data);

    void initEmitter(Long userId, SseEmitter savedEmitter);

    String getRandomEmitterId(Long userId);
}

