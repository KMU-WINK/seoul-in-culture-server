package com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.$chat_message.exception;

import org.springframework.http.HttpStatus;

import com.github.kmu_wink.seoul_in_culture.common.api.ApiException;

public class ChatMessageNotFoundException extends ApiException {

	public ChatMessageNotFoundException() {

		super(HttpStatus.NOT_FOUND, "채팅을 찾을 수 없습니다.");
	}
}
