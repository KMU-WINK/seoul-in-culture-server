package com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.service;

import com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.$chat_message.repository.ChatMessageRepository;
import com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.$review.repository.MeetingReviewRepository;
import com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.dto.response.GetMyMeetingsResponse;
import com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.dto.response.MyMeetingResponse;
import com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.exception.MeetingException;
import com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.repository.MeetingRepository;
import com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.schema.Meeting;
import com.github.kmu_wink.seoul_in_culture.domain.user.exception.UserException;
import com.github.kmu_wink.seoul_in_culture.domain.user.repository.UserRepository;
import com.github.kmu_wink.seoul_in_culture.domain.user.schema.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.exception.MeetingExceptions.MEETING_NOT_FOUND;
import static com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.exception.MeetingExceptions.NOT_HOST;
import static com.github.kmu_wink.seoul_in_culture.domain.user.exception.UserExceptions.USER_NOT_FOUND;
import static com.github.kmu_wink.seoul_in_culture.domain.user.exception.UserExceptions.USER_NOT_PARTICIPANT;

@Service
@RequiredArgsConstructor
public class MeetingService {

    private final UserRepository userRepository;
    private final MeetingRepository meetingRepository;
    private final MeetingReviewRepository meetingReviewRepository;
    private final ChatMessageRepository chatMessageRepository;

    public GetMyMeetingsResponse getMyMeetings(User user, boolean end) {

        return GetMyMeetingsResponse.builder()
                .meetings(meetingRepository.findAllByParticipantsContainingAndEnd(user, end))
                .build();
    }

    public MyMeetingResponse endMeeting(User user, String meetingId) {

        Meeting meeting = meetingRepository.findById(meetingId)
                .filter(x -> x.getParticipants().contains(user))
                .orElseThrow(() -> MeetingException.of(MEETING_NOT_FOUND));

        if (!meeting.getHost().equals(user)) throw MeetingException.of(NOT_HOST);

        meeting.setEnd(true);

        meeting = meetingRepository.save(meeting);

        return MyMeetingResponse.builder()
                .meeting(meeting)
                .build();
    }

    public MyMeetingResponse delegateHost(User user, String meetingId, String targetId) {

        Meeting meeting = meetingRepository.findById(meetingId)
                .filter(x -> x.getParticipants().contains(user))
                .orElseThrow(() -> MeetingException.of(MEETING_NOT_FOUND));

        if (!meeting.getHost().equals(user)) throw MeetingException.of(NOT_HOST);

        User target = userRepository.findById(targetId).orElseThrow(() -> UserException.of(USER_NOT_FOUND));
        if (!meeting.getParticipants().contains(target)) throw UserException.of(USER_NOT_PARTICIPANT);

        meeting.setHost(target);

        meeting = meetingRepository.save(meeting);

        return MyMeetingResponse.builder()
                .meeting(meeting)
                .build();
    }

    public void deleteMeeting(User user, String meetingId) {

        Meeting meeting = meetingRepository.findById(meetingId)
                .filter(x -> x.getParticipants().contains(user))
                .orElseThrow(() -> MeetingException.of(MEETING_NOT_FOUND));

        if (!meeting.getHost().equals(user)) throw MeetingException.of(NOT_HOST);

        chatMessageRepository.deleteAllByMeeting(meeting);
        meetingReviewRepository.deleteAllByMeeting(meeting);
        meetingRepository.delete(meeting);
    }
}
