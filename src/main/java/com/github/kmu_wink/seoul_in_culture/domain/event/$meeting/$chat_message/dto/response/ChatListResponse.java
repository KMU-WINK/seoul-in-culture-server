package com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.$chat_message.dto.response;

import java.util.List;

import com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.$chat_message.schema.ChatMessage;

import lombok.Builder;

@Builder
public record ChatListResponse(

	List<ChatMessage> messages
) {
}
