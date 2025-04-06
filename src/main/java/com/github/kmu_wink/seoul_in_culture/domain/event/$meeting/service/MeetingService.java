package com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.service;

import com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.$participant.repository.MeetingParticipantRepository;
import com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.repository.MeetingRepository;
import com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.schema.Meeting;
import com.github.kmu_wink.seoul_in_culture.domain.event.repository.EventRepository;
import com.github.kmu_wink.seoul_in_culture.domain.user.repository.UserRepository;
import com.github.kmu_wink.seoul_in_culture.domain.user.schema.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MeetingService {
    private final MeetingParticipantRepository meetingParticipantRepository;
    private final UserRepository userRepository;
    private final MeetingRepository meetingRepository;
    private final EventRepository eventRepository;

    public void leaveMeeting(User user,String meeting_id) {
        Meeting meeting = meetingRepository.findById(meeting_id)
                .orElseThrow(() -> new RuntimeException("meeting not found"));

        if (!meetingParticipantRepository.existsByMeetingAndUser(meeting, user)) {
            throw new IllegalStateException("참여하지 않은 모임입니다.");
        }
        meetingParticipantRepository.deleteByMeetingAndUser(meeting, user);

    }


}
