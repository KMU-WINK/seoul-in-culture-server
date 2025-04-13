package com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.$chat_message.dto.request;

import jakarta.validation.constraints.NotBlank;

public record SendChatRequest(

        @NotBlank
        String content
) {
}
