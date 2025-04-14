package com.github.kmu_wink.seoul_in_culture.domain.notification.schema.detail;

import com.github.kmu_wink.seoul_in_culture.domain.meeting.schema.Meeting;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.DBRef;

@Data
@Builder
public class MeetingHostDelegateDetail implements NotificationDetail {

    @DBRef
    Meeting meeting;
}
