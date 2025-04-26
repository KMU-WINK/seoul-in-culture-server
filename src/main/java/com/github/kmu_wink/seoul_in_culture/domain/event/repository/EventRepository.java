package com.github.kmu_wink.seoul_in_culture.domain.event.repository;

import com.github.kmu_wink.seoul_in_culture.domain.event.schema.Event;
import com.github.kmu_wink.seoul_in_culture.domain.user.schema.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EventRepository extends MongoRepository<Event, String> {

    default List<Event> findFilteredEvents(
            MongoTemplate mongoTemplate,
            LocalDate filterDate,
            List<Event.Category> categories,
            List<User.District> districts,
            Boolean isFree
    ) {
        Query query = new Query();

        if (filterDate != null) {
            query.addCriteria(Criteria.where("startDate").lte(filterDate).and("endDate").gt(filterDate));
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

        query.with(Sort.by(Sort.Order.desc("startDate"), Sort.Order.asc("endDate")));

        return mongoTemplate.find(query, Event.class);
    }
}
