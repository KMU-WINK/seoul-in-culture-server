package com.github.kmu_wink.seoul_in_culture.domain.meeting.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MeetingExceptions {

    MEETING_NOT_FOUND("모임을 찾을 수 없습니다."),
    MEETING_NOT_OWNER("모임의 주최자가 아닙니다."),
    MEETING_ALREADY_JOINED("이미 참가한 모임입니다."),
    MEETING_NOT_JOINED("참가하지 않은 모임입니다."),
    MEETING_FULL("모임이 가득 찼습니다."),
    MEETING_NOT_SATISFIED("모임 조건에 충족하지 않습니다."),
    MEETING_ENDED("모임이 종료되었습니다."),
    MEETING_HOST_CANNOT_LEAVE("주최자는 모임을 나갈 수 없습니다."),
    MEETING_DATE_OUT_OF_EVENT_PERIOD("모임 날짜가 행사 기간을 벗어났습니다."),
    MEETING_PAYMENT_FAIL("모임 참가 보증금 결제에 실패했습니다."),
    MEETING_PAYMENT_NOT_EXISTS("모임 참가 보증금 결제 내역이 없습니다.")
    ;

    private final String message;
}
