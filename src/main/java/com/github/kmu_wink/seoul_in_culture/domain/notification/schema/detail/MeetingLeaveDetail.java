package com.github.kmu_wink.seoul_in_culture.domain.notification.schema.detail;

import com.github.kmu_wink.seoul_in_culture.domain.meeting.schema.Meeting;
import com.github.kmu_wink.seoul_in_culture.domain.user.schema.User;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.DBRef;

@Data
@Builder
public class MeetingLeaveDetail implements NotificationDetail {

    Meeting meeting;

    @DBRef(lazy = true)
    User user;
}
