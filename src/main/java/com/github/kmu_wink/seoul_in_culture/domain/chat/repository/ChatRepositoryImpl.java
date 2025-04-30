package com.github.kmu_wink.seoul_in_culture.domain.chat.repository;

import com.github.kmu_wink.seoul_in_culture.domain.chat.schema.Chat;
import com.github.kmu_wink.seoul_in_culture.domain.meeting.schema.Meeting;
import com.github.kmu_wink.seoul_in_culture.domain.user.schema.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.github.kmu_wink.seoul_in_culture.common.mongo.MongoConfig.LATEST_SORT;
import static com.github.kmu_wink.seoul_in_culture.common.mongo.MongoConfig.OLDER_SORT;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.limit;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.lookup;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.sort;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.unwind;
import static org.springframework.data.mongodb.core.query.Criteria.where;

@Repository
@RequiredArgsConstructor
public class ChatRepositoryImpl implements ChatRepository {

    private final MongoOperations operations;

    @Override
    public Optional<Chat> findById(String chattingId) {

        List<Chat> results = operations.aggregate(
                newAggregation(
                        match(where("_id").is(chattingId)),

                        lookup("meeting", "meeting.$id", "_id", "meeting"),
                        unwind("meeting"),
                        lookup("event", "meeting.event.$id", "_id", "meeting.event"),
                        unwind("meeting.event"),
                        lookup("user", "meeting.host.$id", "_id", "meeting.host"),
                        unwind("meeting.host"),
                        lookup("user", "meeting.participants.$id", "_id", "meeting.participants"),

                        lookup("user", "user.$id", "_id", "user"),
                        unwind("user"),

                        lookup("user", "unread.$id", "_id", "unread")
                ), Chat.class, Chat.class
        ).getMappedResults();

        if (results.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(results.getFirst());
        }
    }

    @Override
    public Optional<Chat> findFirstByMeeting(Meeting meeting) {

        List<Chat> results = operations.aggregate(
                newAggregation(
                        match(where("meeting").is(meeting)),

                        sort(LATEST_SORT),
                        limit(1),

                        lookup("meeting", "meeting.$id", "_id", "meeting"),
                        unwind("meeting"),
                        lookup("event", "meeting.event.$id", "_id", "meeting.event"),
                        unwind("meeting.event"),
                        lookup("user", "meeting.host.$id", "_id", "meeting.host"),
                        unwind("meeting.host"),
                        lookup("user", "meeting.participants.$id", "_id", "meeting.participants"),

                        lookup("user", "user.$id", "_id", "user"),
                        unwind("user"),

                        lookup("user", "unread.$id", "_id", "unread")
                ), Chat.class, Chat.class
        ).getMappedResults();

        if (results.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(results.getFirst());
        }
    }

    @Override
    public List<Chat> findAllByMeeting(Meeting meeting) {

        return operations.aggregate(
                newAggregation(
                        match(where("meeting").is(meeting)),

                        sort(OLDER_SORT),
                        limit(1000),

                        lookup("meeting", "meeting.$id", "_id", "meeting"),
                        unwind("meeting"),
                        lookup("event", "meeting.event.$id", "_id", "meeting.event"),
                        unwind("meeting.event"),
                        lookup("user", "meeting.host.$id", "_id", "meeting.host"),
                        unwind("meeting.host"),
                        lookup("user", "meeting.participants.$id", "_id", "meeting.participants"),

                        lookup("user", "user.$id", "_id", "user"),
                        unwind("user"),

                        lookup("user", "unread.$id", "_id", "unread")
                ), Chat.class, Chat.class
        ).getMappedResults();
    }

    @Override
    public long countUnreadByMeeting(Meeting meeting, User user) {

        return operations.count(new Query(where("meeting").is(meeting).and("unread").in(user)), Chat.class);
    }

    @Override
    public void readAll(Meeting meeting, User user) {

        operations.updateMulti(
                new Query(where("meeting").is(meeting).and("unread").in(user)),
                new Update().pull("unread", user),
                Chat.class
        );
    }

    @Override
    public Chat save(Chat chat) {

        return operations.save(chat);
    }
}
