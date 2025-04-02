package com.github.kmu_wink.seoul_in_culture.domain.notification.schema;

import com.github.kmu_wink.seoul_in_culture.common.database.mongo.BaseSchema;
import com.github.kmu_wink.seoul_in_culture.domain.notification.schema.detail.NotificationDetail;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Notification extends BaseSchema {

	Type type;

	NotificationDetail detail;

	boolean unread;

	public enum Type {

		MEETING_JOIN,
		MEETING_LEAVE,
		MEETING_HOST_DELEGATE,
		MEETING_REVIEW
	}
}