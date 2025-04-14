package com.github.kmu_wink.seoul_in_culture.domain.chat.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ChatExceptions {

    MESSAGE_NOT_FOUND("메시지를 찾을 수 없습니다."),
    ;

    private final String message;
}
