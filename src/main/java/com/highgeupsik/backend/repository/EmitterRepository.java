package com.highgeupsik.backend.repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Repository
public class EmitterRepository {

    private final Map<String, SseEmitter> clients = new ConcurrentHashMap<>();

    public void saveEmitter(String emitterId, SseEmitter emitter) {
        clients.put(emitterId, emitter);
    }

    public void deleteById(String id) {
        clients.remove(id);
    }

    public void deleteByUserId(Long userId) {
        clients.forEach((id, emitter) -> {
            if (id.startsWith(String.valueOf(userId))) {
                clients.remove(id);
            }
        });
    }

    public Map<String, SseEmitter> findByUserId(Long userId) {
        return clients.entrySet().stream()
            .filter(entry -> entry.getKey().startsWith(String.valueOf(userId)))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
