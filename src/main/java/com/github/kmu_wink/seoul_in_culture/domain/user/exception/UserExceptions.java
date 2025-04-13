package com.github.kmu_wink.seoul_in_culture.domain.user.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserExceptions {

    USER_NOT_FOUND("유저를 찾을 수 없습니다."),
    ;

    private final String message;
}
