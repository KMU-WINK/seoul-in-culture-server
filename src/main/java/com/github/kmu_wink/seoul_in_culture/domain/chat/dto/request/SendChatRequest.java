package com.github.kmu_wink.seoul_in_culture.domain.chat.dto.request;

import jakarta.validation.constraints.NotBlank;

public record SendChatRequest(

        @NotBlank
        String content
) {
}
