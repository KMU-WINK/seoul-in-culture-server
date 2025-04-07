package com.github.kmu_wink.seoul_in_culture.domain.notification.schema;

import org.springframework.data.mongodb.core.mapping.DBRef;

import com.github.kmu_wink.seoul_in_culture.common.database.mongo.BaseSchema;
import com.github.kmu_wink.seoul_in_culture.domain.notification.schema.detail.NotificationDetail;
import com.github.kmu_wink.seoul_in_culture.domain.user.schema.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Builder
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Notification extends BaseSchema {

	Type type;

	NotificationDetail detail;

	@DBRef
	User user;

	boolean unread;

	public enum Type {

		MEETING_JOIN,
		MEETING_LEAVE,
		MEETING_HOST_DELEGATE,
		MEETING_REVIEW
	}
}