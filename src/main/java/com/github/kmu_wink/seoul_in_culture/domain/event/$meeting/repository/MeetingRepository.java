package com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.repository;

import com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.schema.Meeting;
import com.github.kmu_wink.seoul_in_culture.domain.event.schema.Event;
import com.github.kmu_wink.seoul_in_culture.domain.user.schema.User;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface MeetingRepository extends MongoRepository<Meeting, String> {

	List<Meeting> findAllByHost(User user);

    List<Meeting> findAllByParticipantsContaining(User user);
    List<Meeting> findAllByParticipantsContainingAndEnd(User user, boolean end);

    int countByHost(User user);
    int countByParticipantsContaining(User user);

    default List<Meeting> findFilteredMeetings(
            MongoTemplate mongoTemplate,
            Event event,
            Integer minAge,
            Integer maxAge,
            User.Gender gender
    ) {

        Query query = new Query();

        query.addCriteria(Criteria.where("event").is(event));

        List<Criteria> criteriaList = new ArrayList<>();

        if (minAge != null) {
            criteriaList.add(new Criteria().orOperator(Criteria.where("minAge").is(null), Criteria.where("minAge").gte(minAge)));
        }

        if (maxAge != null) {
            criteriaList.add(new Criteria().orOperator(Criteria.where("maxAge").is(null), Criteria.where("maxAge").lte(maxAge)));
        }

        if (gender != null) {
            criteriaList.add(new Criteria().orOperator(Criteria.where("gender").is(null), Criteria.where("gender").is(gender)));
        }

        if (!criteriaList.isEmpty()) {
            query.addCriteria(new Criteria().andOperator(criteriaList.toArray(new Criteria[0])));
        }

        return mongoTemplate.find(query, Meeting.class);
    }
}
