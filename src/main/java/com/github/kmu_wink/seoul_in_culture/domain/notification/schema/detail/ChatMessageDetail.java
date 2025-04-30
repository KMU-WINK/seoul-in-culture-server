package com.github.kmu_wink.seoul_in_culture.domain.notification.schema.detail;

import com.github.kmu_wink.seoul_in_culture.domain.chat.schema.Chat;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChatMessageDetail implements NotificationDetail {

    Chat message;
}
