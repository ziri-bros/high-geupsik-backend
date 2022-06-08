package com.highgeupsik.backend.api;

import static com.highgeupsik.backend.utils.ApiUtils.error;
import static org.springframework.http.HttpStatus.*;

import com.highgeupsik.backend.exception.MailException;
import com.highgeupsik.backend.exception.ResourceNotFoundException;
import com.highgeupsik.backend.exception.SseException;
import com.highgeupsik.backend.exception.UserException;
import com.highgeupsik.backend.exception.TokenException;
import com.highgeupsik.backend.utils.ApiError;
import com.highgeupsik.backend.utils.ApiResult;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ApiExceptionController {

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler
    public ApiResult<ApiError> resourceNotFoundException(ResourceNotFoundException e) {
        return error(new ApiError("NOT FOUND", e.getMessage()));
    }

    @ResponseStatus(FORBIDDEN)
    @ExceptionHandler
    public ApiResult<ApiError> userException(UserException e) {
        return error(new ApiError("FORBIDDEN", e.getMessage()));
    }

    @ResponseStatus(UNAUTHORIZED)
    @ExceptionHandler
    public ApiResult<ApiError> tokenExpiredException(TokenException e) {
        return error(new ApiError("UNAUTHORIZED", e.getMessage()));
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler
    public ApiResult<ApiError> beanValidException(BindException e) {
        List<String> messages = e.getBindingResult()
            .getAllErrors()
            .stream()
            .map(DefaultMessageSourceResolvable::getDefaultMessage)
            .collect(Collectors.toList());
        return error(new ApiError("BAD", messages.toString()));
    }

    @ResponseStatus(SERVICE_UNAVAILABLE)
    @ExceptionHandler
    public ApiResult<ApiError> sseConnectException(SseException e) {
        return error(new ApiError("SERVICE UNAVAILABLE", e.getMessage()));
    }

    @ResponseStatus(INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public ApiResult<ApiError> mailException(MailException e) {
        log.error("mailException = {}", e);
        return error(new ApiError("SERVER ERROR", e.getMessage()));
    }
}
