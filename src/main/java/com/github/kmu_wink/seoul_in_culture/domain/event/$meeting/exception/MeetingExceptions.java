package com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MeetingExceptions {

    MEETING_NOT_FOUND("모임을 찾을 수 없습니다."),
    NOT_HOST("모임장이 아닙니다."),
    ;

    private final String message;
}
