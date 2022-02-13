package com.highgeupsik.backend.api;

import static com.highgeupsik.backend.utils.ApiUtils.error;
import static org.springframework.http.HttpStatus.*;

import com.highgeupsik.backend.exception.DuplicateException;
import com.highgeupsik.backend.exception.MailException;
import com.highgeupsik.backend.exception.NotFoundException;
import com.highgeupsik.backend.exception.NotMatchException;
import com.highgeupsik.backend.exception.TokenException;
import com.highgeupsik.backend.utils.ApiError;
import com.highgeupsik.backend.utils.ApiResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorApiController {

    @ExceptionHandler(NotFoundException.class)
    public ApiResult notFoundException(NotFoundException e) {
        return error(new ApiError(e.getMessage(), INTERNAL_SERVER_ERROR));
    }

    @ExceptionHandler(NotMatchException.class)
    public ApiResult notMatchException(NotMatchException e) {
        return error(new ApiError(e.getMessage(), FORBIDDEN));
    }

    @ExceptionHandler(DuplicateException.class)
    public ApiResult duplicateException(DuplicateException e) {
        return error(new ApiError(e.getMessage(), INTERNAL_SERVER_ERROR));
    }

    @ExceptionHandler(TokenException.class)
    public ApiResult tokenExpiredException(TokenException e) {
        return error(new ApiError(e.getMessage(), FORBIDDEN));
    }

    @ExceptionHandler(MailException.class)
    public ApiResult messagingException(MailException e) {
        return error(new ApiError(e.getMessage(), INTERNAL_SERVER_ERROR));
    }
}
