package com.highgeupsik.backend.api;

import com.highgeupsik.backend.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class MessageApiController {

    private final MessageService messageService;
}
