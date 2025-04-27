package com.github.kmu_wink.seoul_in_culture.domain.notification.api;

import com.github.kmu_wink.seoul_in_culture.domain.notification.repository.FcmTokenRepository;
import com.github.kmu_wink.seoul_in_culture.domain.notification.repository.NotificationRepository;
import com.github.kmu_wink.seoul_in_culture.domain.notification.schema.Notification;
import com.github.kmu_wink.seoul_in_culture.domain.notification.schema.detail.NotificationDetail;
import com.github.kmu_wink.seoul_in_culture.domain.user.schema.User;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MessagingErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationApi {

    private final NotificationRepository notificationRepository;
    private final FcmTokenRepository fcmTokenRepository;

    @Async
    public void sendNotification(User user, NotificationDetail detail) {

        Notification.Type type = Notification.Type.fromDetail(detail);

        Notification notification = notificationRepository.save(Notification.builder()
                .user(user)
                .type(type)
                .detail(detail)
                .url(Notification.createUrl(type, detail))
                .unread(true)
                .build());

        fcmTokenRepository.findAllByUser(user).forEach(token -> {

            Message message = Message.builder()
                    .setToken(token)
                    .putData("title", notification.getTitle())
                    .putData("body", notification.getBody())
                    .putData("url", notification.getUrl())
                    .build();

            try {
                FirebaseMessaging.getInstance().send(message);
            } catch (FirebaseMessagingException e) {
                if (!e.getMessagingErrorCode().equals(MessagingErrorCode.UNREGISTERED)) {
                    throw new RuntimeException(e);
                }
                fcmTokenRepository.deleteByToken(token);
            }
        });
    }
}
