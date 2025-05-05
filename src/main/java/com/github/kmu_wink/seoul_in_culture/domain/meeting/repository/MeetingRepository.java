package com.github.kmu_wink.seoul_in_culture.domain.meeting.repository;

import com.github.kmu_wink.seoul_in_culture.domain.event.schema.Event;
import com.github.kmu_wink.seoul_in_culture.domain.meeting.schema.Meeting;
import com.github.kmu_wink.seoul_in_culture.domain.user.schema.User;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface MeetingRepository {

    Optional<Meeting> findById(String meetingId);

    List<Meeting> findAllByHost(User user);
    List<Meeting> findAllByParticipants(User user);
    List<Meeting> findAllByParticipantsAndEnd(User user, boolean end);
    List<Meeting> findMeetingsWithFilter(Event event, Integer minAge, Integer maxAge, User.Gender gender);

    Map<String, Integer> countByEvents(Collection<Event> events);
    long countByParticipantsContaining(User user);

    Meeting save(Meeting meeting);

    void delete(Meeting meeting);

    void clearExpiredBoosts(LocalDateTime beforeTime);
}