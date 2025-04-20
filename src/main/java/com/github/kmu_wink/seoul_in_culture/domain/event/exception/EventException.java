package com.github.kmu_wink.seoul_in_culture.domain.event.exception;

import com.github.kmu_wink.seoul_in_culture.common.api.ApiException;

public class EventException extends ApiException {

    private EventException(EventExceptions eventExceptions) {

        super(eventExceptions.getMessage());
    }

    public static EventException of(EventExceptions eventExceptions) {

        return new EventException(eventExceptions);
    }
}
