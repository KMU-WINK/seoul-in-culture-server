package com.github.kmu_wink.seoul_in_culture.common.api;

import java.util.Map;

public record ApiResponse<T>(
        boolean success,
        String errorMessage,
        T content
) {

    public static <T> ApiResponse<T> ok() {

        return ok(null);
    }

    public static <T> ApiResponse<T> ok(T content) {

        return new ApiResponse<>(true, null, content);
    }

    public static ApiResponse<Map<String, String>> error(ApiException exception) {

        return error(exception.getMessage());
    }

    public static ApiResponse<Map<String, String>> error(String message) {

        return new ApiResponse<>(false, message, null);
    }
}
