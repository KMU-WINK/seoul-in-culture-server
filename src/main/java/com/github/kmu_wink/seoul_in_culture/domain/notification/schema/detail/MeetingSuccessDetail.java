package com.github.kmu_wink.seoul_in_culture.domain.notification.schema.detail;

import com.github.kmu_wink.seoul_in_culture.domain.meeting.schema.Meeting;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MeetingSuccessDetail implements NotificationDetail {

    Meeting meeting;
}
