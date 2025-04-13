package com.github.kmu_wink.seoul_in_culture.domain.notification.service;

import com.github.kmu_wink.seoul_in_culture.domain.notification.dto.response.GetNotificationsResponse;
import com.github.kmu_wink.seoul_in_culture.domain.notification.exception.NotificationException;
import com.github.kmu_wink.seoul_in_culture.domain.notification.repository.NotificationRepository;
import com.github.kmu_wink.seoul_in_culture.domain.notification.schema.Notification;
import com.github.kmu_wink.seoul_in_culture.domain.user.schema.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.github.kmu_wink.seoul_in_culture.domain.notification.exception.NotificationExceptions.NOTIFICATION_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public GetNotificationsResponse getNotifications(User user) {

        return GetNotificationsResponse.builder()
                .notifications(notificationRepository.findAllByUser(user))
                .build();
    }

    public void readNotification(User user, String notificationId) {

        Notification notification = notificationRepository.findById(notificationId)
                .filter(x -> x.getUser().equals(user))
                .orElseThrow(() -> NotificationException.of(NOTIFICATION_NOT_FOUND));

        notification.setUnread(false);

        notificationRepository.save(notification);
    }

    public void readAllNotification(User user) {

        notificationRepository.findAllByUserAndUnreadIsTrue(user)
                .forEach(notification -> {
                    notification.setUnread(false);
                    notificationRepository.save(notification);
                });
    }
}
