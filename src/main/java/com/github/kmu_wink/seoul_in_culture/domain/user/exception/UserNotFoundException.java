package com.github.kmu_wink.seoul_in_culture.domain.user.exception;

import com.github.kmu_wink.seoul_in_culture.common.api.ApiException;
import org.springframework.http.HttpStatus;

public class UserNotFoundException extends ApiException {
    public UserNotFoundException() {
        super(HttpStatus.NOT_FOUND, "해당 유저가 존재하지 않습니다.");
    }
}