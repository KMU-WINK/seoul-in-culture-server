package com.github.kmu_wink.seoul_in_culture.domain.bookmark.repository;

import com.github.kmu_wink.seoul_in_culture.domain.bookmark.schema.Bookmark;
import com.github.kmu_wink.seoul_in_culture.domain.event.schema.Event;
import com.github.kmu_wink.seoul_in_culture.domain.user.schema.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.github.kmu_wink.seoul_in_culture.common.mongo.MongoConfig.LATEST_SORT;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.limit;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.lookup;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.replaceRoot;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.sort;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.unwind;
import static org.springframework.data.mongodb.core.query.Criteria.where;

@Repository
@RequiredArgsConstructor
public class BookmarkRepositoryImpl implements BookmarkRepository {

    private final MongoOperations operations;

    @Override
    public List<Event> findAllByUser(User user) {

        return operations.aggregate(
                newAggregation(
                        match(where("user").is(user)),

                        sort(LATEST_SORT),

                        lookup("event", "event.$id", "_id", "event"),
                        unwind("event"),

                        replaceRoot("event")
                ), Bookmark.class, Event.class
        ).getMappedResults();
    }

    @Override
    public List<Event> findTop2ByUser(User user) {

        return operations.aggregate(
                newAggregation(
                        match(where("user").is(user)),

                        sort(LATEST_SORT),
                        limit(2),

                        lookup("event", "event.$id", "_id", "event"),
                        unwind("event"),

                        replaceRoot("event")
                ), Bookmark.class, Event.class
        ).getMappedResults();
    }

    @Override
    public long countByUser(User user) {

        return operations.count(new Query().addCriteria(where("user").is(user)), Bookmark.class);
    }

    @Override
    public boolean existsByUserAndEvent(User user, Event event) {

        return operations.exists(
                new Query().addCriteria(where("user").is(user).and("event").is(event)),
                Bookmark.class
        );
    }

    @Override
    public Event save(Bookmark bookmark) {

        return operations.save(bookmark).getEvent();
    }

    @Override
    public void deleteByUserAndEvent(User user, Event event) {

        operations.remove(new Query().addCriteria(where("user").is(user).and("event").is(event)), Bookmark.class);
    }
}
