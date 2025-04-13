package com.github.kmu_wink.seoul_in_culture.domain.event.repository;

import com.github.kmu_wink.seoul_in_culture.domain.event.schema.Event;
import com.github.kmu_wink.seoul_in_culture.domain.user.schema.User;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

@Repository
public interface EventRepository extends MongoRepository<Event, String> {

    default List<Event> findFilteredEvents(
            MongoTemplate mongoTemplate,
            LocalDate date,
            List<Event.Category> categories,
            List<User.District> districts,
            Boolean isFree
    ) {

        Query query = new Query();

        if (date != null) {
            query.addCriteria(Criteria.where("startDate").lte(date).and("endDate").gte(date));
        }

        if (categories != null && !categories.isEmpty()) {
            query.addCriteria(Criteria.where("category").in(categories));
        }

        if (districts != null && !districts.isEmpty()) {
            query.addCriteria(Criteria.where("district").in(districts));
        }

        if (isFree != null) {
            query.addCriteria(Criteria.where("free").is(isFree));
        }

        List<Event> events = mongoTemplate.find(query, Event.class);

        LocalDate today = LocalDate.now();

        Comparator<Event> comparator = (e1, e2) -> {
            boolean e1Future = !e1.getStartDate().isBefore(today);
            boolean e2Future = !e2.getStartDate().isBefore(today);

            if (e1Future && e2Future) {
                return e1.getStartDate().compareTo(e2.getStartDate());
            } else if (!e1Future && !e2Future) {
                return e2.getStartDate().compareTo(e1.getStartDate());
            } else if (e1Future) {
                return -1;
            } else {
                return 1;
            }
        };

        events.sort(comparator);

        return events;
    }
}
