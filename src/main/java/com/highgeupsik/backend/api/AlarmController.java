package com.highgeupsik.backend.api;

import com.highgeupsik.backend.resolver.LoginUser;
import com.highgeupsik.backend.service.AlarmService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RequiredArgsConstructor
@RestController
public class AlarmController {

    private final AlarmService alarmService;

    @GetMapping(value = "/subscribe", produces = "text/event-stream")
    public SseEmitter subscribe(@LoginUser Long userId) {
        return alarmService.subscribe(userId);
    }
}