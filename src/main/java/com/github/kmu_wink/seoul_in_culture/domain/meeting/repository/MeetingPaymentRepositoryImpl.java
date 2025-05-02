package com.github.kmu_wink.seoul_in_culture.domain.meeting.repository;

import com.github.kmu_wink.seoul_in_culture.domain.meeting.schema.Meeting;
import com.github.kmu_wink.seoul_in_culture.domain.meeting.schema.MeetingPayment;
import com.github.kmu_wink.seoul_in_culture.domain.user.schema.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.lookup;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.unwind;
import static org.springframework.data.mongodb.core.query.Criteria.where;

@Repository
@RequiredArgsConstructor
public class MeetingPaymentRepositoryImpl implements MeetingPaymentRepository {

    private final MongoOperations operations;

    @Override
    public Optional<MeetingPayment> findByUserAndMeeting(User user, Meeting meeting) {

        List<MeetingPayment> results = operations.aggregate(
                newAggregation(
                        match(where("user").is(user).and("meeting").is(meeting)),

                        lookup("meeting", "meeting.$id", "_id", "meeting"),
                        unwind("meeting"),
                        lookup("event", "meeting.event.$id", "_id", "meeting.event"),
                        unwind("meeting.event"),
                        lookup("user", "meeting.host.$id", "_id", "meeting.host"),
                        unwind("meeting.host"),
                        lookup("user", "meeting.participants.$id", "_id", "meeting.participants"),

                        lookup("user", "user.$id", "_id", "user"),
                        unwind("user")
                ),
                MeetingPayment.class,
                MeetingPayment.class
        ).getMappedResults();

        if (results.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(results.getFirst());
        }
    }

    @Override
    public MeetingPayment save(MeetingPayment meetingPayment) {

        return operations.save(meetingPayment);
    }

    @Override
    public void delete(MeetingPayment meetingPayment) {

        operations.remove(meetingPayment);
    }
}
