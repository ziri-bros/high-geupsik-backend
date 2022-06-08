package com.highgeupsik.backend.service;

import static com.highgeupsik.backend.utils.ErrorMessage.SSE_CONNECT_ERROR;

import com.highgeupsik.backend.repository.EmitterRepository;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@RequiredArgsConstructor
@Service
public class AlarmServiceImpl implements AlarmService {

    private final EmitterRepository emitterRepository;

    @Override
    public SseEmitter subscribe(Long userId) {
        SseEmitter emitter = new SseEmitter(TIME_OUT);
        String emitterId = getRandomEmitterId(userId);
        initEmitter(userId, emitter);
        emitterRepository.saveEmitter(emitterId, emitter);
        sendToClient(emitter, emitterId, "서버와 연결되었습니다. userId = " + userId);
        return emitter;
    }

    @Override
    public void sendToClient(SseEmitter emitter, String id, Object data) {
        try {
            emitter.send(SseEmitter.event()
                .id(id)
                .name("sse")
                .data(data));
        } catch (IOException exception) {
            emitterRepository.deleteById(id);
            throw new RuntimeException(SSE_CONNECT_ERROR);
        }
    }

    @Override
    public void send(Long userId, Object data) {
        Map<String, SseEmitter> emitters = emitterRepository.findByUserId(userId);
        emitters.forEach((emitterId, emitter) -> sendToClient(emitter, emitterId, data));
    }

    @Override
    public void initEmitter(Long userId, SseEmitter savedEmitter) {
        savedEmitter.onTimeout(() -> emitterRepository.deleteByUserId(userId));
        savedEmitter.onCompletion(() -> emitterRepository.deleteByUserId(userId));
    }

    @Override
    public String getRandomEmitterId(Long userId) {
        return userId + "_" + UUID.randomUUID();
    }
}

