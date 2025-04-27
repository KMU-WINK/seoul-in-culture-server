package com.github.kmu_wink.seoul_in_culture.domain.notification.repository;

import com.github.kmu_wink.seoul_in_culture.domain.notification.schema.Notification;
import com.github.kmu_wink.seoul_in_culture.domain.user.schema.User;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository {

    Optional<Notification> findById(String id);

    List<Notification> findAllByUser(User user);

    void readAllNotification(User user);

    Notification save(Notification notification);
}
