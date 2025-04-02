package com.github.kmu_wink.seoul_in_culture.domain.auth.exception;

import org.springframework.http.HttpStatus;

import com.github.kmu_wink.seoul_in_culture.common.api.ApiException;

public class AccessTokenExpiredException extends ApiException {
    public AccessTokenExpiredException() {

        super(HttpStatus.UNAUTHORIZED, "액세스 토큰이 만료되었습니다.");
    }
}
