package com.highgeupsik.backend.utils;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiError {

    String message;
    HttpStatus httpStatus;

    public ApiError(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
