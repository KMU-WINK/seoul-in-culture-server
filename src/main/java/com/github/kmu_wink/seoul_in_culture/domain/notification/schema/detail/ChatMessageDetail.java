package com.github.kmu_wink.seoul_in_culture.domain.notification.schema.detail;

import com.github.kmu_wink.seoul_in_culture.domain.chat.schema.Chat;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.DBRef;

@Data
@Builder
public class ChatMessageDetail implements NotificationDetail {

    @DBRef(lazy = true)
    Chat message;
}
