package com.github.kmu_wink.seoul_in_culture.domain.notification.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NotificationExceptions {

    NOTIFICATION_NOT_FOUND("알림을 찾을 수 없습니다."),
    OTHER_USER_NOTIFICATION("다른 유저의 알림입니다.");

    private final String message;
}
