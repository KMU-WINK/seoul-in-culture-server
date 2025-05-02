package com.github.kmu_wink.seoul_in_culture.domain.meeting.service;

import com.github.kmu_wink.seoul_in_culture.common.toss.TossApi;
import com.github.kmu_wink.seoul_in_culture.domain.event.exception.EventException;
import com.github.kmu_wink.seoul_in_culture.domain.event.repository.EventRepository;
import com.github.kmu_wink.seoul_in_culture.domain.event.schema.Event;
import com.github.kmu_wink.seoul_in_culture.domain.meeting.dto.request.CreateMeetingRequest;
import com.github.kmu_wink.seoul_in_culture.domain.meeting.dto.response.GetMeetingResponse;
import com.github.kmu_wink.seoul_in_culture.domain.meeting.dto.response.GetMeetingsResponse;
import com.github.kmu_wink.seoul_in_culture.domain.meeting.exception.MeetingException;
import com.github.kmu_wink.seoul_in_culture.domain.meeting.repository.MeetingPaymentRepository;
import com.github.kmu_wink.seoul_in_culture.domain.meeting.repository.MeetingRepository;
import com.github.kmu_wink.seoul_in_culture.domain.meeting.schema.Meeting;
import com.github.kmu_wink.seoul_in_culture.domain.meeting.schema.MeetingPayment;
import com.github.kmu_wink.seoul_in_culture.domain.notification.api.NotificationApi;
import com.github.kmu_wink.seoul_in_culture.domain.notification.schema.detail.MeetingHostDelegateDetail;
import com.github.kmu_wink.seoul_in_culture.domain.notification.schema.detail.MeetingJoinDetail;
import com.github.kmu_wink.seoul_in_culture.domain.notification.schema.detail.MeetingLeaveDetail;
import com.github.kmu_wink.seoul_in_culture.domain.notification.schema.detail.MeetingSuccessDetail;
import com.github.kmu_wink.seoul_in_culture.domain.user.exception.UserException;
import com.github.kmu_wink.seoul_in_culture.domain.user.repository.UserRepository;
import com.github.kmu_wink.seoul_in_culture.domain.user.schema.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import static com.github.kmu_wink.seoul_in_culture.domain.event.exception.EventExceptions.EVENT_NOT_FOUND;
import static com.github.kmu_wink.seoul_in_culture.domain.meeting.exception.MeetingExceptions.MEETING_ALREADY_JOINED;
import static com.github.kmu_wink.seoul_in_culture.domain.meeting.exception.MeetingExceptions.MEETING_DATE_OUT_OF_EVENT_PERIOD;
import static com.github.kmu_wink.seoul_in_culture.domain.meeting.exception.MeetingExceptions.MEETING_ENDED;
import static com.github.kmu_wink.seoul_in_culture.domain.meeting.exception.MeetingExceptions.MEETING_FULL;
import static com.github.kmu_wink.seoul_in_culture.domain.meeting.exception.MeetingExceptions.MEETING_HOST_CANNOT_LEAVE;
import static com.github.kmu_wink.seoul_in_culture.domain.meeting.exception.MeetingExceptions.MEETING_NOT_FOUND;
import static com.github.kmu_wink.seoul_in_culture.domain.meeting.exception.MeetingExceptions.MEETING_NOT_JOINED;
import static com.github.kmu_wink.seoul_in_culture.domain.meeting.exception.MeetingExceptions.MEETING_NOT_OWNER;
import static com.github.kmu_wink.seoul_in_culture.domain.meeting.exception.MeetingExceptions.MEETING_NOT_SATISFIED;
import static com.github.kmu_wink.seoul_in_culture.domain.user.exception.UserExceptions.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class MeetingService {

    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final MeetingRepository meetingRepository;
    private final MeetingPaymentRepository meetingPaymentRepository;

    private final NotificationApi notificationApi;
    private final TossApi tossApi;

    public GetMeetingsResponse getMyMeetings(User user, boolean active) {

        List<Meeting> meetings = meetingRepository.findAllByParticipantsAndEnd(user, !active);

        return GetMeetingsResponse.builder().meetings(meetings).build();
    }

    public GetMeetingsResponse getMeetings(String eventId, Integer minAge, Integer maxAge, User.Gender gender) {

        Event event = eventRepository.findById(eventId).orElseThrow(() -> EventException.of(EVENT_NOT_FOUND));

        List<Meeting> meetings = meetingRepository.findMeetingsWithFilter(event, minAge, maxAge, gender);

        return GetMeetingsResponse.builder().meetings(meetings).build();
    }

    public GetMeetingResponse getMeeting(String meetingId) {

        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> MeetingException.of(MEETING_NOT_FOUND));

        return GetMeetingResponse.builder().meeting(meeting).build();
    }

    public GetMeetingResponse createMeeting(User user, String eventId, CreateMeetingRequest dto) {

        Event event = eventRepository.findById(eventId).orElseThrow(() -> EventException.of(EVENT_NOT_FOUND));

        if (dto.date().toLocalDate().isBefore(event.getStartDate()) ||
                dto.date().toLocalDate().isAfter(event.getEndDate())) {

            throw MeetingException.of(MEETING_DATE_OUT_OF_EVENT_PERIOD);
        }

        if ((Objects.nonNull(dto.maxAge()) && user.getAge() > dto.maxAge()) ||
                (Objects.nonNull(dto.minAge()) && user.getAge() < dto.minAge()) ||
                (Objects.nonNull(dto.gender()) && !user.getGender().equals(dto.gender()))) {

            throw MeetingException.of(MEETING_NOT_SATISFIED);
        }

        Meeting meeting = meetingRepository.save(Meeting.builder()
                .event(event)
                .title(dto.title())
                .description(dto.description())
                .date(dto.date())
                .maxPeople(dto.maxPeople())
                .minAge(dto.minAge())
                .maxAge(dto.maxAge())
                .gender(dto.gender())
                .host(user)
                .participants(Set.of(user))
                .end(false)
                .build());

        return GetMeetingResponse.builder().meeting(meeting).build();
    }

    public GetMeetingResponse joinMeeting(User user, String meetingId, String orderId, String paymentKey, int amount) {

        Meeting meeting = meetingRepository.findById(meetingId).stream().peek(x -> {
            if (x.getParticipants().contains(user)) {
                throw MeetingException.of(MEETING_ALREADY_JOINED);
            }
        }).peek(x -> {
            if (x.getParticipants().size() + 1 > x.getMaxPeople()) {
                throw MeetingException.of(MEETING_FULL);
            }
        }).peek(x -> {
            if (Objects.nonNull(x.getMaxAge()) && user.getAge() > x.getMaxAge()) {
                throw MeetingException.of(MEETING_NOT_SATISFIED);
            }
        }).peek(x -> {
            if (Objects.nonNull(x.getMinAge()) && user.getAge() < x.getMinAge()) {
                throw MeetingException.of(MEETING_NOT_SATISFIED);
            }
        }).peek(x -> {
            if (Objects.nonNull(x.getGender()) && !user.getGender().equals(x.getGender())) {
                throw MeetingException.of(MEETING_NOT_SATISFIED);
            }
        }).peek(x -> {
            if (x.isEnd()) {
                throw MeetingException.of(MEETING_ENDED);
            }
        }).findFirst().orElseThrow(() -> MeetingException.of(MEETING_NOT_FOUND));

        String paymentKey2 = tossApi.confirmPayment(orderId, paymentKey, amount);

        meetingPaymentRepository.save(MeetingPayment.builder()
                .meeting(meeting)
                .user(user)
                .paymentKey(paymentKey2)
                .build());

        meeting.getParticipants().add(user);

        Meeting finalMeeting = meeting;
        meeting.getParticipants().forEach(participant -> {
            if (participant.equals(user)) {
                return;
            }

            notificationApi.sendNotification(
                    participant,
                    MeetingJoinDetail.builder().meeting(finalMeeting).user(user).build()
            );
        });

        meeting = meetingRepository.save(meeting);

        return GetMeetingResponse.builder().meeting(meeting).build();
    }

    public GetMeetingResponse leaveMeeting(User user, String meetingId) {

        Meeting meeting = meetingRepository.findById(meetingId).stream().peek(x -> {
            if (!x.getParticipants().contains(user)) {
                throw MeetingException.of(MEETING_NOT_JOINED);
            }
        }).peek(x -> {
            if (x.getHost().equals(user)) {
                throw MeetingException.of(MEETING_HOST_CANNOT_LEAVE);
            }
        }).peek(x -> {
            if (x.isEnd()) {
                throw MeetingException.of(MEETING_ENDED);
            }
        }).findFirst().orElseThrow(() -> MeetingException.of(MEETING_NOT_FOUND));

        refund(user, meeting);
        meeting.getParticipants().remove(user);

        Meeting finalMeeting = meeting;
        meeting.getParticipants()
                .forEach(participant -> notificationApi.sendNotification(
                        participant,
                        MeetingLeaveDetail.builder().meeting(finalMeeting).user(user).build()
                ));

        meeting = meetingRepository.save(meeting);

        return GetMeetingResponse.builder().meeting(meeting).build();
    }

    public GetMeetingResponse finishMeeting(User user, String meetingId, List<String> attendantId) {

        List<User> attendantUsers = attendantId.stream().map(userRepository::findById).peek(u -> {
            if (u.isEmpty()) {
                throw UserException.of(USER_NOT_FOUND);
            }
        }).map(Optional::get).toList();

        Meeting meeting = meetingRepository.findById(meetingId).stream().peek(x -> {
            if (!x.getParticipants().contains(user)) {
                throw MeetingException.of(MEETING_NOT_JOINED);
            }
        }).peek(x -> {
            if (!x.getHost().equals(user)) {
                throw MeetingException.of(MEETING_NOT_OWNER);
            }
        }).peek(x -> {
            if (x.isEnd()) {
                throw MeetingException.of(MEETING_ENDED);
            }
        }).findFirst().orElseThrow(() -> MeetingException.of(MEETING_NOT_FOUND));

        meeting.setEnd(true);

        meeting = meetingRepository.save(meeting);

        Meeting finalMeeting = meeting;
        attendantUsers.forEach(u -> refund(u, finalMeeting));

        meeting.getParticipants()
                .forEach(participant -> notificationApi.sendNotification(
                        participant,
                        MeetingSuccessDetail.builder().meeting(finalMeeting).build()
                ));

        return GetMeetingResponse.builder().meeting(meeting).build();
    }

    public GetMeetingResponse delegateHost(User user, String meetingId, String targetId) {

        Meeting meeting = meetingRepository.findById(meetingId).stream().peek(x -> {
            if (!x.getParticipants().contains(user)) {
                throw MeetingException.of(MEETING_NOT_JOINED);
            }
        }).peek(x -> {
            if (!x.getHost().equals(user)) {
                throw MeetingException.of(MEETING_NOT_OWNER);
            }
        }).peek(x -> {
            if (x.isEnd()) {
                throw MeetingException.of(MEETING_ENDED);
            }
        }).findFirst().orElseThrow(() -> MeetingException.of(MEETING_NOT_FOUND));

        User target = userRepository.findById(targetId).orElseThrow(() -> UserException.of(USER_NOT_FOUND));
        if (!meeting.getParticipants().contains(target)) {
            throw MeetingException.of(MEETING_NOT_JOINED);
        }

        meeting.setHost(target);

        meeting = meetingRepository.save(meeting);

        Meeting finalMeeting = meeting;
        meeting.getParticipants()
                .forEach(participant -> notificationApi.sendNotification(
                        participant,
                        MeetingHostDelegateDetail.builder().meeting(finalMeeting).build()
                ));

        return GetMeetingResponse.builder().meeting(meeting).build();
    }

    public void deleteMeeting(User user, String meetingId) {

        Meeting meeting = meetingRepository.findById(meetingId).stream().peek(x -> {
            if (!x.getParticipants().contains(user)) {
                throw MeetingException.of(MEETING_NOT_JOINED);
            }
        }).peek(x -> {
            if (!x.getHost().equals(user)) {
                throw MeetingException.of(MEETING_NOT_OWNER);
            }
        }).peek(x -> {
            if (x.isEnd()) {
                throw MeetingException.of(MEETING_ENDED);
            }
        }).findFirst().orElseThrow(() -> MeetingException.of(MEETING_NOT_FOUND));

        meeting.getParticipants().forEach(u -> refund(u, meeting));

        meetingRepository.delete(meeting);
    }

    private void refund(User user, Meeting meeting) {

        meetingPaymentRepository.findByUserAndMeeting(user, meeting).ifPresent(payment -> {
            tossApi.refundPayment(payment.getPaymentKey());
            meetingPaymentRepository.delete(payment);
        });
    }
}
