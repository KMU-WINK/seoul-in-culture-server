package com.github.kmu_wink.seoul_in_culture.domain.notification.repository;

import com.github.kmu_wink.seoul_in_culture.domain.notification.schema.Notification;
import com.github.kmu_wink.seoul_in_culture.domain.user.schema.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.github.kmu_wink.seoul_in_culture.common.mongo.MongoConfig.LATEST_SORT;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.lookup;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.sort;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.unwind;
import static org.springframework.data.mongodb.core.query.Criteria.where;

@Repository
@RequiredArgsConstructor
public class NotificationRepositoryImpl implements NotificationRepository {

    private final MongoOperations operations;

    @Override
    public Optional<Notification> findById(String id) {

        List<Notification> results = operations.aggregate(
                Aggregation.newAggregation(
                        match(where("_id").is(id)),

                        lookup("user", "user.$id", "_id", "user"),
                        unwind("user"),

                        lookup("user", "detail.user.$id", "_id", "detail.user"),
                        unwind("detail.user", true),

                        lookup("meeting", "detail.meeting.$id", "_id", "detail.meeting"),
                        unwind("detail.meeting", true),

                        lookup("event", "detail.meeting.event.$id", "_id", "detail.meeting.event"),
                        unwind("detail.meeting.event", true),

                        lookup("user", "detail.meeting.host.$id", "_id", "detail.meeting.host"),
                        unwind("detail.meeting.host", true),

                        lookup("user", "detail.meeting.participants.$id", "_id", "detail.meeting.participants")
                ), Notification.class, Notification.class
        ).getMappedResults();

        if (results.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(results.getFirst());
        }
    }

    @Override
    public List<Notification> findAllByUser(User user) {

        return operations.aggregate(
                Aggregation.newAggregation(
                        match(where("user").is(user).and("type").ne(Notification.Type.CHAT_MESSAGE)),

                        sort(LATEST_SORT),

                        lookup("user", "user.$id", "_id", "user"),
                        unwind("user"),

                        lookup("user", "detail.user.$id", "_id", "detail.user"),
                        unwind("detail.user", true),

                        lookup("meeting", "detail.meeting.$id", "_id", "detail.meeting"),
                        unwind("detail.meeting", true),

                        lookup("event", "detail.meeting.event.$id", "_id", "detail.meeting.event"),
                        unwind("detail.meeting.event", true),

                        lookup("user", "detail.meeting.host.$id", "_id", "detail.meeting.host"),
                        unwind("detail.meeting.host", true),

                        lookup("user", "detail.meeting.participants.$id", "_id", "detail.meeting.participants")
                ), Notification.class, Notification.class
        ).getMappedResults();
    }

    @Override
    public void readAllNotification(User user) {

        operations.updateMulti(
                new Query().addCriteria(where("user").is(user).and("unread").is(true)),
                new Update().set("unread", false),
                Notification.class
        );
    }

    @Override
    public Notification save(Notification notification) {

        return operations.save(notification);
    }
}
