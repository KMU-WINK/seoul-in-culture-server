package com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.service;

import static com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.exception.MeetingExceptions.*;
import static com.github.kmu_wink.seoul_in_culture.domain.event.exception.EventExceptions.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.dto.request.CreateMeetingRequest;
import com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.dto.response.GetMeetingResponse;
import com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.dto.response.GetMeetingsResponse;
import com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.exception.MeetingException;
import com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.repository.MeetingRepository;
import com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.schema.Meeting;
import com.github.kmu_wink.seoul_in_culture.domain.event.exception.EventException;
import com.github.kmu_wink.seoul_in_culture.domain.event.repository.EventRepository;
import com.github.kmu_wink.seoul_in_culture.domain.event.schema.Event;
import com.github.kmu_wink.seoul_in_culture.domain.user.schema.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MeetingService {

	private final MeetingRepository meetingRepository;
	private final EventRepository eventRepository;

	private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

	public GetMeetingsResponse getMyMeetings(User user, boolean active) {

		List<Meeting> meetings = meetingRepository.findAllByParticipantsContainingAndEnd(user, active);

		return GetMeetingsResponse.builder()
			.meetings(meetings)
			.build();
	}

	public GetMeetingsResponse getMeetings(String eventId) {

		Event event = eventRepository.findById(eventId).orElseThrow(() -> EventException.of(EVENT_NOT_FOUND));

		List<Meeting> meetings = meetingRepository.findAllByEvent(event);

		return GetMeetingsResponse.builder()
			.meetings(meetings)
			.build();
	}

	public GetMeetingResponse getMeeting(String meetingId) {

		Meeting meeting = meetingRepository.findById(meetingId)
			.orElseThrow(() -> MeetingException.of(MEETING_NOT_FOUND));

		return GetMeetingResponse.builder()
			.meeting(meeting)
			.build();
	}

	public GetMeetingResponse createMeeting(User user, String eventId, CreateMeetingRequest dto) {

		Event event = eventRepository.findById(eventId)
			.orElseThrow(() -> EventException.of(EVENT_NOT_FOUND));

		Meeting meeting = meetingRepository.save(
			Meeting.builder()
				.event(event)
				.title(dto.title())
				.description(dto.description())
				.date(LocalDateTime.parse(dto.datetime(), dateTimeFormatter))
				.maxPeople(dto.maxPeople())
				.minAge(dto.minAge())
				.maxAge(dto.maxAge())
				.gender(dto.gender())
				.host(user)
				.participants(Set.of(user))
				.end(false)
				.build()
		);

		return GetMeetingResponse.builder()
			.meeting(meeting)
			.build();
	}

	public void joinMeeting(User user, String meetingId) {

		Meeting meeting = meetingRepository.findById(meetingId)
			.stream()
			.peek(x -> {
				if (x.getParticipants().contains(user)) throw MeetingException.of(MEETING_ALREADY_JOINED);
			})
			.peek(x -> {
				if (x.getParticipants().size() + 1 > x.getMaxPeople()) throw MeetingException.of(MEETING_FULL);
			})
			.peek(x -> {
				if (Objects.nonNull(x.getMaxAge()) && user.getAge() > x.getMaxAge()) throw MeetingException.of(MEETING_NOT_SATISFIED);
			})
			.peek(x -> {
				if (Objects.nonNull(x.getMinAge()) && user.getAge() < x.getMinAge()) throw MeetingException.of(MEETING_NOT_SATISFIED);
			})
			.peek(x -> {
				if (Objects.nonNull(x.getGender()) && !user.getGender().equals(x.getGender())) throw MeetingException.of(MEETING_NOT_SATISFIED);
			})
			.peek(x -> {
				if (x.isEnd()) throw MeetingException.of(MEETING_ENDED);
			})
			.findFirst()
			.orElseThrow(() -> MeetingException.of(MEETING_NOT_FOUND));

		meeting.getParticipants().add(user);

		meetingRepository.save(meeting);
	}

	public void leaveMeeting(User user, String meetingId) {

		Meeting meeting = meetingRepository.findById(meetingId)
			.stream()
			.peek(x -> {
				if (!x.getParticipants().contains(user)) throw MeetingException.of(MEETING_NOT_JOINED);
			})
			.peek(x -> {
				if (x.getHost().equals(user)) throw MeetingException.of(MEETING_HOST_CANNOT_LEAVE);
			})
			.findFirst()
			.orElseThrow(() -> MeetingException.of(MEETING_NOT_FOUND));

		meeting.getParticipants().remove(user);

		meetingRepository.save(meeting);
	}
}
