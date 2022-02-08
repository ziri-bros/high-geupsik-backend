package com.highgeupsik.backend.utils;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

@Getter
@Slf4j
public class ApiError {

    String message;
    HttpStatus httpStatus;

    public ApiError(String message, HttpStatus httpStatus) {
        log.error(message);
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
