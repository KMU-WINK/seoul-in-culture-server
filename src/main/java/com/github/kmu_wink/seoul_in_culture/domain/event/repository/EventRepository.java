package com.github.kmu_wink.seoul_in_culture.domain.event.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.github.kmu_wink.seoul_in_culture.domain.event.schema.Event;

@Repository
public interface EventRepository extends MongoRepository<Event, String> {
}
