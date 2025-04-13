package com.github.kmu_wink.seoul_in_culture.domain.notification.exception;

import com.github.kmu_wink.seoul_in_culture.common.api.ApiException;

public class NotificationException extends ApiException {

    private NotificationException(
            NotificationExceptions notificationExceptions) {

        super(notificationExceptions.getMessage());
    }

    public static NotificationException of(NotificationExceptions notificationExceptions) {

        return new NotificationException(notificationExceptions);
    }
}
