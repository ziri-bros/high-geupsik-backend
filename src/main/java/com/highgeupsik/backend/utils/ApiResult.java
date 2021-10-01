package com.highgeupsik.backend.utils;


import lombok.Getter;

@Getter
public class ApiResult<T> {

    private boolean success;
    private T data;
    private ApiError error;

    public ApiResult(boolean success, T data, ApiError error) {
        this.success = success;
        this.data = data;
        this.error = error;
    }

}
