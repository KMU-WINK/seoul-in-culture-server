package com.github.kmu_wink.seoul_in_culture.domain.notification.service;

import com.github.kmu_wink.seoul_in_culture.domain.notification.dto.request.SubscribeRequest;
import com.github.kmu_wink.seoul_in_culture.domain.notification.dto.response.GetNotificationsResponse;
import com.github.kmu_wink.seoul_in_culture.domain.notification.exception.NotificationException;
import com.github.kmu_wink.seoul_in_culture.domain.notification.repository.FcmTokenRepository;
import com.github.kmu_wink.seoul_in_culture.domain.notification.repository.NotificationRepository;
import com.github.kmu_wink.seoul_in_culture.domain.notification.schema.FcmToken;
import com.github.kmu_wink.seoul_in_culture.domain.notification.schema.Notification;
import com.github.kmu_wink.seoul_in_culture.domain.user.schema.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.github.kmu_wink.seoul_in_culture.domain.notification.exception.NotificationExceptions.NOTIFICATION_NOT_FOUND;
import static com.github.kmu_wink.seoul_in_culture.domain.notification.exception.NotificationExceptions.OTHER_USER_NOTIFICATION;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final FcmTokenRepository fcmTokenRepository;

    public GetNotificationsResponse getNotifications(User user) {

        return GetNotificationsResponse.builder()
                .notifications(notificationRepository.findAllByUserOrderByIdDesc(user))
                .build();
    }

    public void readNotification(User user, String notificationId) {

        Notification notification = notificationRepository.findById(notificationId).stream().peek(x -> {
            if (!x.getUser().equals(user)) {
                throw NotificationException.of(OTHER_USER_NOTIFICATION);
            }
        }).findFirst().orElseThrow(() -> NotificationException.of(NOTIFICATION_NOT_FOUND));

        notification.setUnread(false);

        notificationRepository.save(notification);
    }

    public void readAllNotification(User user) {

        notificationRepository.findAllByUserAndUnreadIsTrue(user).forEach(notification -> {
            notification.setUnread(false);
            notificationRepository.save(notification);
        });
    }

    public void subscribe(User user, SubscribeRequest dto) {

        fcmTokenRepository.findByUser(user).ifPresentOrElse(
                fcmToken -> {
                    fcmToken.setToken(dto.token());
                    fcmTokenRepository.save(fcmToken);
                }, () -> fcmTokenRepository.save(FcmToken.builder().user(user).token(dto.token()).build())
        );
    }
}
