package com.github.kmu_wink.seoul_in_culture.common.api;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ApiResponse<?> apiException(ApiException e) {

        return ApiResponse.error(e);
    }
}
