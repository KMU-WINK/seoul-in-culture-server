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
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Repository
public interface EventRepository extends MongoRepository<Event, String> {

    default List<Event> findFilteredEvents(
            MongoTemplate mongoTemplate,
            LocalDate filterDate,
            List<Event.Category> categories,
            List<User.District> districts,
            Boolean isFree
    ) {

        Function<Query, Query> applyBaseFilters = query -> {
            if (filterDate != null) {
                query.addCriteria(Criteria.where("startDate").lte(filterDate).and("endDate").gte(filterDate));
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
            return query;
        };

        LocalDate today = LocalDate.now();
        int limit = 100;

        Query futureQuery = applyBaseFilters.apply(new Query());
        futureQuery.addCriteria(Criteria.where("startDate").gte(today));
        futureQuery.with(Sort.by(Sort.Direction.ASC, "startDate")).limit(limit);
        List<Event> future = mongoTemplate.find(futureQuery, Event.class);

        if (future.size() >= limit) {
            return future;
        }

        int remaining = limit - future.size();
        Query pastQuery = applyBaseFilters.apply(new Query());
        pastQuery.addCriteria(Criteria.where("startDate").lt(today));
        pastQuery.with(Sort.by(Sort.Direction.DESC, "startDate")).limit(remaining);
        List<Event> past = mongoTemplate.find(pastQuery, Event.class);

        List<Event> result = new ArrayList<>(limit);
        result.addAll(future);
        result.addAll(past);
        return result;
    }
}
