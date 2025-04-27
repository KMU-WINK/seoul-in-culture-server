package com.github.kmu_wink.seoul_in_culture.domain.event.repository;

import com.github.kmu_wink.seoul_in_culture.domain.event.schema.Event;
import com.github.kmu_wink.seoul_in_culture.domain.user.schema.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.springframework.data.domain.Sort.Order.asc;
import static org.springframework.data.domain.Sort.Order.desc;
import static org.springframework.data.domain.Sort.by;
import static org.springframework.data.mongodb.core.query.Criteria.where;

@Repository
@RequiredArgsConstructor
public class EventRepositoryImpl implements EventRepository {

    private final MongoOperations operations;

    @Override
    public Optional<Event> findById(String id) {

        return Optional.ofNullable(operations.findById(id, Event.class));
    }

    @Override
    public List<Event> findAll() {

        return operations.findAll(Event.class);
    }

    @Override
    public List<Event> findAllWithFilter(
            LocalDate date,
            List<Event.Category> categories,
            List<User.District> districts,
            Boolean isFree
    ) {

        Query query = new Query();

        if (date != null) {
            query.addCriteria(where("startDate").lte(date).and("endDate").gt(date));
        }

        if (categories != null && !categories.isEmpty()) {
            query.addCriteria(where("category").in(categories));
        }

        if (districts != null && !districts.isEmpty()) {
            query.addCriteria(where("district").in(districts));
        }

        if (isFree != null) {
            query.addCriteria(where("free").is(isFree));
        }

        query.with(by(desc("startDate"), asc("endDate")));
        query.limit(1000);

        return operations.find(query, Event.class);
    }

    @Override
    public Collection<Event> saveAll(Collection<Event> events) {

        return operations.insertAll(events);
    }
}
