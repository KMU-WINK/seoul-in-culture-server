package com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.$chat_message.dto.response;

import java.util.Collection;

import com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.$chat_message.schema.ChatMessage;
import com.github.kmu_wink.seoul_in_culture.domain.user.schema.User;

import lombok.Builder;

@Builder
public record ChatInfoResponse(

	Collection<User> participants,
	Collection<ChatMessage> messages
) {
}
