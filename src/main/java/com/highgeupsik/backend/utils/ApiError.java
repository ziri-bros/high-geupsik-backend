package com.highgeupsik.backend.utils;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public class ApiError {

    String code;
    String message;

    public ApiError(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
