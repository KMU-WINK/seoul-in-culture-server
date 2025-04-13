package com.github.kmu_wink.seoul_in_culture.domain.notification.schema.detail;

import com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.schema.Meeting;
import org.springframework.data.mongodb.core.mapping.DBRef;

public class MeetingHostDelegateDetail implements NotificationDetail {

    @DBRef
    Meeting meeting;
}
