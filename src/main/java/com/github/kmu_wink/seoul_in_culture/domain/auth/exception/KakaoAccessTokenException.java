package com.github.kmu_wink.seoul_in_culture.domain.auth.exception;

import org.springframework.http.HttpStatus;

import com.github.kmu_wink.seoul_in_culture.common.api.ApiException;

public class KakaoAccessTokenException extends ApiException {

    public KakaoAccessTokenException(String message) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, message);
    }
}
