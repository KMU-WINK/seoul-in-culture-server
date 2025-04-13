package com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.$chat_message.dto.response;

import com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.$chat_message.schema.ChatMessage;
import lombok.Builder;

@Builder
public record SendChatResponse(

        ChatMessage message
) {
}
