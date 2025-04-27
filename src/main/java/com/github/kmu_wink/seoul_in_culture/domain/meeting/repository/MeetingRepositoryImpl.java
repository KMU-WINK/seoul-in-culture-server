package com.github.kmu_wink.seoul_in_culture.domain.meeting.repository;

import com.github.kmu_wink.seoul_in_culture.domain.event.schema.Event;
import com.github.kmu_wink.seoul_in_culture.domain.meeting.schema.Meeting;
import com.github.kmu_wink.seoul_in_culture.domain.user.schema.User;
import com.mongodb.DBRef;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.github.kmu_wink.seoul_in_culture.common.mongo.MongoConfig.LATEST_SORT;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.lookup;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.sort;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.unwind;
import static org.springframework.data.mongodb.core.query.Criteria.where;

@Repository
@RequiredArgsConstructor
public class MeetingRepositoryImpl implements MeetingRepository {

    private final MongoOperations operations;

    @Override
    public Optional<Meeting> findById(String meetingId) {

        List<Meeting> results = operations.aggregate(
                newAggregation(
                        match(where("_id").is(meetingId)),
                        lookup("event", "event.$id", "_id", "event"),
                        unwind("event"),
                        lookup("user", "host.$id", "_id", "host"),
                        unwind("host"),
                        lookup("user", "participants.$id", "_id", "participants")
                ), Meeting.class, Meeting.class
        ).getMappedResults();

        if (results.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(results.getFirst());
        }
    }

    @Override
    public List<Meeting> findAllByHost(User user) {

        return operations.aggregate(
                newAggregation(
                        match(where("host").is(user)),

                        sort(LATEST_SORT),

                        lookup("event", "event.$id", "_id", "event"), unwind("event"),

                        lookup("user", "host.$id", "_id", "host"), unwind("host"),

                        lookup("user", "participants.$id", "_id", "participants")
                ), Meeting.class, Meeting.class
        ).getMappedResults();
    }

    @Override
    public List<Meeting> findAllByParticipants(User user) {

        return operations.aggregate(
                newAggregation(
                        match(where("participants").in(user)),

                        sort(LATEST_SORT),

                        lookup("event", "event.$id", "_id", "event"), unwind("event"),

                        lookup("user", "host.$id", "_id", "host"), unwind("host"),

                        lookup("user", "participants.$id", "_id", "participants")
                ), Meeting.class, Meeting.class
        ).getMappedResults();
    }

    @Override
    public List<Meeting> findAllByParticipantsAndEnd(User user, boolean end) {

        return operations.aggregate(
                newAggregation(
                        match(where("participants").in(user).and("end").is(end)),

                        sort(LATEST_SORT),

                        lookup("event", "event.$id", "_id", "event"), unwind("event"),

                        lookup("user", "host.$id", "_id", "host"), unwind("host"),

                        lookup("user", "participants.$id", "_id", "participants")
                ), Meeting.class, Meeting.class
        ).getMappedResults();
    }

    @Override
    public List<Meeting> findMeetingsWithFilter(Event event, Integer minAge, Integer maxAge, User.Gender gender) {

        Criteria criteria = Criteria.where("event").is(event);

        if (minAge != null) {
            criteria.andOperator(new Criteria().orOperator(
                    Criteria.where("minAge").is(null),
                    Criteria.where("minAge").gte(minAge)
            ));
        }

        if (maxAge != null) {
            criteria.andOperator(new Criteria().orOperator(
                    Criteria.where("maxAge").is(null),
                    Criteria.where("maxAge").lte(maxAge)
            ));
        }

        if (gender != null) {
            criteria.andOperator(new Criteria().orOperator(
                    Criteria.where("gender").is(null),
                    Criteria.where("gender").is(gender)
            ));
        }

        return operations.aggregate(
                newAggregation(
                        match(criteria),

                        sort(LATEST_SORT),

                        lookup("event", "event.$id", "_id", "event"), unwind("event"),

                        lookup("user", "host.$id", "_id", "host"), unwind("host"),

                        lookup("user", "participants.$id", "_id", "participants")
                ), Meeting.class, Meeting.class
        ).getMappedResults();
    }

    @Override
    public Map<String, Integer> countByEvents(Collection<Event> events) {

        List<String> eventIds = events.stream().map(Event::getId).toList();

        return operations.aggregate(
                        newAggregation(match(where("event").in(eventIds)), group("event").count().as("meetings")),
                        Meeting.class,
                        Document.class
                )
                .getMappedResults()
                .stream()
                .collect(Collectors.toMap(
                        doc -> doc.get("_id", DBRef.class).getId().toString(),
                        doc -> doc.get("meetings", Integer.class)
                ));
    }

    @Override
    public long countByParticipantsContaining(User user) {

        return operations.count(new Query().addCriteria(where("participants").in(user)), Meeting.class);
    }

    @Override
    public Meeting save(Meeting meeting) {

        return operations.save(meeting);
    }

    @Override
    public void delete(Meeting meeting) {

        operations.remove(meeting);
    }
}
