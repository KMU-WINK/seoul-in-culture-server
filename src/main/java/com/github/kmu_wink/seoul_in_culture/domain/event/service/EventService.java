package com.github.kmu_wink.seoul_in_culture.domain.event.service;

import com.github.kmu_wink.seoul_in_culture.domain.event.dto.response.EventsResponse;
import com.github.kmu_wink.seoul_in_culture.domain.event.repository.EventRepository;
import com.github.kmu_wink.seoul_in_culture.domain.event.schema.Event;
import com.github.kmu_wink.seoul_in_culture.domain.user.schema.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;

    private final MongoTemplate mongoTemplate;

    public EventsResponse getEvents(
            LocalDate date,
            List<Event.Category> categories,
            List<User.District> districts,
            Boolean isFree
    ) {

        return EventsResponse.builder()
                .events(eventRepository.findFilteredEvents(mongoTemplate, date, categories, districts, isFree))
                .build();
    }
}
