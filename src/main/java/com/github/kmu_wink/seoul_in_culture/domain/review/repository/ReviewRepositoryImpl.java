package com.github.kmu_wink.seoul_in_culture.domain.review.repository;

import com.github.kmu_wink.seoul_in_culture.domain.meeting.schema.Meeting;
import com.github.kmu_wink.seoul_in_culture.domain.review.schema.Review;
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
import static org.springframework.data.mongodb.core.aggregation.Aggregation.sort;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.unwind;
import static org.springframework.data.mongodb.core.query.Criteria.where;

@Repository
@RequiredArgsConstructor
public class ReviewRepositoryImpl implements ReviewRepository {

    private final MongoOperations operations;

    @Override
    public List<Review> findAllByTarget(User user) {

        return operations.aggregate(
                newAggregation(
                        match(where("target").is(user)),

                        sort(LATEST_SORT),

                        lookup("meeting", "meeting.$id", "_id", "meeting"),
                        unwind("meeting"),
                        lookup("user", "meeting.host.$id", "_id", "meeting.host"),
                        unwind("meeting.host"),
                        lookup("user", "meeting.participants.$id", "_id", "meeting.participants"),

                        lookup("event", "meeting.event.$id", "_id", "meeting.event"),
                        unwind("meeting.event"),

                        lookup("user", "author.$id", "_id", "author"),
                        unwind("author"),

                        lookup("user", "target.$id", "_id", "target"),
                        unwind("target")
                ), Review.class, Review.class
        ).getMappedResults();
    }

    @Override
    public List<Review> findAllByMeetingAndAuthor(Meeting meeting, User user) {

        return operations.aggregate(
                newAggregation(
                        match(where("meeting").is(meeting).and("author").is(user)),

                        sort(LATEST_SORT),

                        lookup("meeting", "meeting.$id", "_id", "meeting"),
                        unwind("meeting"),
                        lookup("user", "meeting.host.$id", "_id", "meeting.host"),
                        unwind("meeting.host"),
                        lookup("user", "meeting.participants.$id", "_id", "meeting.participants"),

                        lookup("event", "meeting.event.$id", "_id", "meeting.event"),
                        unwind("meeting.event"),

                        lookup("user", "author.$id", "_id", "author"),
                        unwind("author"),

                        lookup("user", "target.$id", "_id", "target"),
                        unwind("target")
                ), Review.class, Review.class
        ).getMappedResults();
    }

    @Override
    public List<Review> findTop2ByTarget(User user) {

        return operations.aggregate(
                newAggregation(
                        match(where("target").is(user)),

                        sort(LATEST_SORT),
                        limit(2),

                        lookup("meeting", "meeting.$id", "_id", "meeting"),
                        unwind("meeting"),
                        lookup("user", "meeting.host.$id", "_id", "meeting.host"),
                        unwind("meeting.host"),
                        lookup("user", "meeting.participants.$id", "_id", "meeting.participants"),

                        lookup("event", "meeting.event.$id", "_id", "meeting.event"),
                        unwind("meeting.event"),

                        lookup("user", "author.$id", "_id", "author"),
                        unwind("author"),

                        lookup("user", "target.$id", "_id", "target"),
                        unwind("target")
                ), Review.class, Review.class
        ).getMappedResults();
    }

    @Override
    public boolean existsByMeetingAndAuthorAndTarget(Meeting meeting, User author, User target) {

        return operations.exists(
                new Query().addCriteria(where("meeting").is(meeting).and("author").is(author).and("target").is(target)),
                Review.class
        );
    }

    @Override
    public Review save(Review review) {

        return operations.save(review);
    }
}
