package com.github.kmu_wink.seoul_in_culture.domain.chat.dto.response;

import com.github.kmu_wink.seoul_in_culture.domain.chat.schema.Chat;
import lombok.Builder;

import java.util.Collection;

@Builder
public record ChatInfoResponse(

        Collection<Chat> chats
) {

}
