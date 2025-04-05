package com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.$chat_message.exception;

import org.springframework.http.HttpStatus;

import com.github.kmu_wink.seoul_in_culture.common.api.ApiException;

public class NotJoiningRoomException extends ApiException {

	public NotJoiningRoomException() {

		super(HttpStatus.UNAUTHORIZED, "참가중인 모임이 아닙니다.");
	}
}
