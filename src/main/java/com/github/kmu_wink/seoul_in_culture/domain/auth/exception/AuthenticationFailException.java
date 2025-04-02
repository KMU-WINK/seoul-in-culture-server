package com.github.kmu_wink.seoul_in_culture.domain.auth.exception;

import org.springframework.http.HttpStatus;

import com.github.kmu_wink.seoul_in_culture.common.api.ApiException;

public class AuthenticationFailException extends ApiException {
    public AuthenticationFailException() {

        super(HttpStatus.UNAUTHORIZED, "인증에 실패하였습니다.");
    }
}
