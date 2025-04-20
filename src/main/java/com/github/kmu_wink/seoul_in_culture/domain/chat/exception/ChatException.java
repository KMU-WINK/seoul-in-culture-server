package com.github.kmu_wink.seoul_in_culture.domain.chat.exception;

import com.github.kmu_wink.seoul_in_culture.common.api.ApiException;

public class ChatException extends ApiException {

    private ChatException(ChatExceptions chatExceptions) {

        super(chatExceptions.getMessage());
    }

    public static ChatException of(ChatExceptions chatExceptions) {

        return new ChatException(chatExceptions);
    }
}
