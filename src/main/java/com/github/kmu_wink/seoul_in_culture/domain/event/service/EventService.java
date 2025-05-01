package com.github.kmu_wink.seoul_in_culture.domain.event.service;

import com.github.kmu_wink.seoul_in_culture.domain.event.dto.response.EventResponse;
import com.github.kmu_wink.seoul_in_culture.domain.event.dto.response.EventsResponse;
import com.github.kmu_wink.seoul_in_culture.domain.event.exception.EventException;
import com.github.kmu_wink.seoul_in_culture.domain.event.repository.EventRepository;
import com.github.kmu_wink.seoul_in_culture.domain.event.schema.Event;
import com.github.kmu_wink.seoul_in_culture.domain.meeting.repository.MeetingRepository;
import com.github.kmu_wink.seoul_in_culture.domain.user.schema.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static com.github.kmu_wink.seoul_in_culture.domain.event.exception.EventExceptions.EVENT_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final MeetingRepository meetingRepository;

    public EventsResponse getEvents(
            LocalDate date,
            String searchQuery,
            List<Event.Category> categories,
            List<User.District> districts,
            Boolean isFree
    ) {

        List<Event> events = eventRepository.findAllWithFilter(date, searchQuery, categories, districts, isFree);
        Map<String, Integer> countMap = meetingRepository.countByEvents(events);

        List<EventsResponse.EventDto> results = events.stream()
                .map(event -> EventsResponse.EventDto.builder()
                        .event(event)
                        .meetings(countMap.getOrDefault(event.getId(), 0))
                        .build())
                .toList();

        return EventsResponse.builder().events(results).build();
    }

    public EventResponse getEvent(String eventId) {

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> EventException.of(EVENT_NOT_FOUND));

        return EventResponse.builder()
                .event(event)
                .build();
    }

    public EventsResponse getAdvertisedEvent() {

        List<Event> events = eventRepository.findTop5ByAdvertised();

        Map<String, Integer> countMap = meetingRepository.countByEvents(events);

        List<EventsResponse.EventDto> results = events.stream()
                .map(event -> EventsResponse.EventDto.builder()
                        .event(event)
                        .meetings(countMap.getOrDefault(event.getId(), 0))
                        .build())
                .toList();

        return EventsResponse.builder().events(results).build();
    }
}