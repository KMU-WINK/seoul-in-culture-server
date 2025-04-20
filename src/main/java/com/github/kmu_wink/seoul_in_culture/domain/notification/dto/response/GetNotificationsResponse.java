package com.github.kmu_wink.seoul_in_culture.domain.notification.dto.response;

import com.github.kmu_wink.seoul_in_culture.domain.notification.schema.Notification;
import lombok.Builder;

import java.util.Collection;

@Builder
public record GetNotificationsResponse(

        Collection<Notification> notifications
) {

}
