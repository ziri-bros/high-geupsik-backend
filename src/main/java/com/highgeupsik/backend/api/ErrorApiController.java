package com.highgeupsik.backend.api;


import com.highgeupsik.backend.exception.DuplicateException;
import com.highgeupsik.backend.exception.NotFoundException;
import com.highgeupsik.backend.exception.NotMatchException;
import com.highgeupsik.backend.exception.TokenExpiredException;
import com.highgeupsik.backend.utils.ApiError;
import com.highgeupsik.backend.utils.ApiResult;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.highgeupsik.backend.utils.ApiUtils.*;

@RestControllerAdvice
public class ErrorApiController {

    @ExceptionHandler(NotFoundException.class)
    public ApiResult notFoundException(NotFoundException e) {
        return error(new ApiError(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR));
    }

    @ExceptionHandler(NotMatchException.class)
    public ApiResult notMatchException(NotMatchException e) {
        return error(new ApiError(e.getMessage(), HttpStatus.FORBIDDEN));
    }

    @ExceptionHandler(DuplicateException.class)
    public ApiResult duplicateException(DuplicateException e){
        return error(new ApiError(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR));
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ApiResult tokenExpiredException(TokenExpiredException e){
        return error(new ApiError(e.getMessage(), HttpStatus.FORBIDDEN));
    }

}
