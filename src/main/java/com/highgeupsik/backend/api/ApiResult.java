package com.highgeupsik.backend.api;

import lombok.Getter;

@Getter
public class ApiResult<T> {

    private boolean success;
    private T data;
    private ApiError error;

    public ApiResult(ApiError error) {
        this.error = error;
    }

    public ApiResult(boolean success, T data) {
        this.success = success;
        this.data = data;
    }
}
