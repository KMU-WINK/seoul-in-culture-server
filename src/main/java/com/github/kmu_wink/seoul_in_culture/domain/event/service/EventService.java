package com.github.kmu_wink.seoul_in_culture.domain.event.service;

import com.github.kmu_wink.seoul_in_culture.domain.event.dto.response.EventsResponse;
import com.github.kmu_wink.seoul_in_culture.domain.event.repository.EventRepository;
import com.github.kmu_wink.seoul_in_culture.domain.event.schema.Event;
import com.github.kmu_wink.seoul_in_culture.domain.meeting.schema.Meeting;
import com.github.kmu_wink.seoul_in_culture.domain.user.schema.User;
import com.mongodb.DBRef;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

        List<Event> events = eventRepository.findFilteredEvents(mongoTemplate, date, categories, districts, isFree);
        List<String> eventIds = events.stream().map(Event::getId).toList();

        Map<String, Integer> countMap = mongoTemplate.aggregate(
                        Aggregation.newAggregation(
                                Aggregation.match(Criteria.where("event").in(eventIds)),
                                Aggregation.group("event").count().as("meetings")
                        ), Meeting.class, Document.class
                )
                .getMappedResults()
                .stream()
                .collect(Collectors.toMap(
                        doc -> doc.get("_id", DBRef.class).getId().toString(),
                        doc -> doc.get("meetings", Integer.class)
                ));

        List<EventsResponse.EventDto> results = events.stream()
                .map(event -> EventsResponse.EventDto.builder()
                        .event(event)
                        .meetings(countMap.getOrDefault(event.getId(), 0))
                        .build())
                .toList();

        return EventsResponse.builder().events(results).build();
    }
}