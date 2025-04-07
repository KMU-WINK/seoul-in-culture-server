package com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.$chat_message.exception;

import com.github.kmu_wink.seoul_in_culture.common.api.ApiException;

public class ChatMessageException extends ApiException {

	private ChatMessageException(
		ChatMessageExceptions chatMessageExceptions) {

		super(chatMessageExceptions.getMessage());
	}

	public static ChatMessageException of(ChatMessageExceptions chatMessageExceptions) {

		return new ChatMessageException(chatMessageExceptions);
	}
}
