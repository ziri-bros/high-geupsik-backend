package com.highgeupsik.backend.api;

import static com.highgeupsik.backend.utils.ApiUtils.error;
import static org.springframework.http.HttpStatus.*;

import com.highgeupsik.backend.exception.MailException;
import com.highgeupsik.backend.exception.ResourceNotFoundException;
import com.highgeupsik.backend.exception.NotWriterException;
import com.highgeupsik.backend.exception.TokenException;
import com.highgeupsik.backend.utils.ApiError;
import com.highgeupsik.backend.utils.ApiResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ApiExceptionController {

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler
    public ApiResult resourceNotFoundException(ResourceNotFoundException e) {
        return error(new ApiError("NOT FOUND", e.getMessage()));
    }

    @ResponseStatus(FORBIDDEN)
    @ExceptionHandler
    public ApiResult notWriterException(NotWriterException e) {
        return error(new ApiError("FORBIDDEN", e.getMessage()));
    }

    @ResponseStatus(UNAUTHORIZED)
    @ExceptionHandler
    public ApiResult tokenExpiredException(TokenException e) {
        return error(new ApiError("UNAUTHORIZED", e.getMessage()));
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler
    public ApiResult illegalArgumentException(IllegalArgumentException e) {
        return error(new ApiError("BAD", e.getMessage()));
    }

    @ResponseStatus(INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public ApiResult mailException(MailException e) {
        log.error("mailException = {}", e);
        return error(new ApiError("SERVER ERROR", e.getMessage()));
    }
}
