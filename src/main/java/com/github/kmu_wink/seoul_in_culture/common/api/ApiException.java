package com.github.kmu_wink.seoul_in_culture.common.api;

public abstract class ApiException extends RuntimeException {

    protected ApiException(String message) {

        super(message);
    }
}
