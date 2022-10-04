package com.highgeupsik.backend.api;

public class ApiUtils {

    public static <T> ApiResult<T> success(T data) {
        return new ApiResult<>(true, data, null);
    }

    public static <T> ApiResult<T> error(ApiError error) {
        return new ApiResult<>(false, null, error);
    }
}
