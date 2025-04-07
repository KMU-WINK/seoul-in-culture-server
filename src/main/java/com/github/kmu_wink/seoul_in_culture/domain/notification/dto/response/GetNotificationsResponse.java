package com.github.kmu_wink.seoul_in_culture.domain.notification.dto.response;

import java.util.Collection;

import com.github.kmu_wink.seoul_in_culture.domain.notification.schema.Notification;

import lombok.Builder;

@Builder
public record GetNotificationsResponse(

	Collection<Notification> notifications
) {
}
