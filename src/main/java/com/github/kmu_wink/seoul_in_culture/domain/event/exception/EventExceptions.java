package com.github.kmu_wink.seoul_in_culture.domain.event.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EventExceptions {
    EVENT_NOT_FOUND("행사를 찾을 수 없습니다."),
    ;

    private final String message;
}
