package com.github.kmu_wink.seoul_in_culture.domain.event.repository;

import com.github.kmu_wink.seoul_in_culture.domain.event.schema.Event;
import com.github.kmu_wink.seoul_in_culture.domain.user.schema.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface EventRepository {

    Optional<Event> findById(String id);

    List<Event> findAll();
    List<Event> findAllWithFilter(
            LocalDate date,
            String searchQuery,
            List<Event.Category> categories,
            List<User.District> districts,
            Boolean isFree
    );

    List<Event> findTop5ByAdvertised();

    Collection<Event> saveAll(Collection<Event> events);
}