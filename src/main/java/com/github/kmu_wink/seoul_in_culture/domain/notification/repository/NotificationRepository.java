package com.github.kmu_wink.seoul_in_culture.domain.notification.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.github.kmu_wink.seoul_in_culture.domain.notification.schema.Notification;
import com.github.kmu_wink.seoul_in_culture.domain.user.schema.User;

@Repository
public interface NotificationRepository extends MongoRepository<Notification, String> {

	List<Notification> findAllByUser(User user);

	List<Notification> findAllByUserAndUnreadIsTrue(User user);
}
