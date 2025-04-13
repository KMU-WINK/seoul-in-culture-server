package com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.exception;

import com.github.kmu_wink.seoul_in_culture.common.api.ApiException;

public class MeetingException extends ApiException {

    private MeetingException(MeetingExceptions meetingExceptions) {

        super(meetingExceptions.getMessage());
    }

    public static MeetingException of(MeetingExceptions meetingExceptions) {

        return new MeetingException(meetingExceptions);
    }
}
