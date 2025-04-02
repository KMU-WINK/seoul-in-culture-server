package com.github.kmu_wink.seoul_in_culture.domain.notification.schema.detail;

import org.springframework.data.mongodb.core.mapping.DBRef;

import com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.schema.Meeting;

public class MeetingHostDelegateDetail implements NotificationDetail {

	@DBRef
	Meeting meeting;
}
