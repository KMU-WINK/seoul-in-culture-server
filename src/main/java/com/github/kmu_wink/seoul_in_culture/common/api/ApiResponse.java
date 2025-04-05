package com.github.kmu_wink.seoul_in_culture.common.api;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.Map;

@Data
public class ApiResponse<T> {

    private final int statusCode;
    private final String errorMessage;
    private final T content;

    private ApiResponse(int statusCode, String errorMessage, T content) {
        this.statusCode = statusCode;
        this.errorMessage = errorMessage;
        this.content = content;
    }

    public static <T> ApiResponse<T> ok() {
        return new ApiResponse<>(200, null, null);
    }

    public static <T> ApiResponse<T> ok(T content) {
        return new ApiResponse<>(200, null, content);
    }

    public static ApiResponse<Map<String, String>> error(HttpStatus status, String message) {
        return new ApiResponse<>(status.value(), message, null);
    }

    public static ApiResponse<Map<String, String>> error(ApiException exception) {
        return error(exception.getStatus(), exception.getMessage());
    }
}
