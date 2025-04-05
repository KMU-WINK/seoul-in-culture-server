package com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.exception;

import org.springframework.http.HttpStatus;

import com.github.kmu_wink.seoul_in_culture.common.api.ApiException;


public class MeetingNotFoundException extends ApiException {

	public MeetingNotFoundException() {

		super(HttpStatus.NOT_FOUND, "모임을 찾을 수 없습니다.");
	}
}
