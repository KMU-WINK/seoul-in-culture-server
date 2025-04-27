package com.github.kmu_wink.seoul_in_culture.domain.notification.repository;

import com.github.kmu_wink.seoul_in_culture.domain.notification.schema.Notification;
import com.github.kmu_wink.seoul_in_culture.domain.user.schema.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends MongoRepository<Notification, String> {

    List<Notification> findAllByUser(User user, Sort sort);

    default void readAllNotification(MongoTemplate mongoTemplate, User user) {

        Query query = new Query();
        query.addCriteria(Criteria.where("user").is(user).and("unread").is(true));

        Update update = new Update();
        update.set("unread", false);

        mongoTemplate.updateMulti(query, update, Notification.class);
    }
}
