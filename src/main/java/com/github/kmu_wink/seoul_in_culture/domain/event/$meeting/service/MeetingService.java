package com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.service;

import com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.$participant.repository.MeetingParticipantRepository;
import com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.$participant.schema.MeetingParticipant;
import com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.dto.MeetingResponse;
import com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.exception.MeetingNotFoundException;
import com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.repository.MeetingRepository;
import com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.schema.Meeting;
import com.github.kmu_wink.seoul_in_culture.domain.event.repository.EventRepository;
import com.github.kmu_wink.seoul_in_culture.domain.user.repository.UserRepository;
import com.github.kmu_wink.seoul_in_culture.domain.user.schema.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MeetingService {
    private final MeetingParticipantRepository meetingParticipantRepository;
    private final MeetingRepository meetingRepository;
    private final UserRepository userRepository;

    public void leaveMeeting(User user,String meeting_id) {
        Meeting meeting = meetingRepository.findById(meeting_id)
                .orElseThrow(MeetingNotFoundException::new);

        if (!meetingParticipantRepository.existsByMeetingAndUser(meeting, user)) {
            throw new IllegalStateException("참여하지 않은 모임입니다.");
        }
        meetingParticipantRepository.deleteByMeetingAndUser(meeting, user);

    }

    public void deleteMeeting(User user, String meeting_id) {
        Meeting meeting = meetingRepository.findById(meeting_id)
                .orElseThrow(MeetingNotFoundException::new);

        MeetingParticipant meetingParticipant = meetingParticipantRepository.findByMeetingAndUser(meeting, user)
                .orElseThrow(() -> new RuntimeException("참가 기록이 없습니다"));


        if(!meetingParticipant.isHost()) {
            throw new IllegalStateException("삭제 권한이 없습니다.");
        }
        meetingParticipantRepository.deleteAllByMeetingId(meeting_id);
        meetingRepository.deleteById(meeting_id);
        log.info("Meeting [{}] and its participants deleted by user [{}]", meeting_id, user.getId());
    }

    public void endMeeting(User user, String meeting_id) {
        Meeting meeting = meetingRepository.findById(meeting_id)
                .orElseThrow(()-> new RuntimeException("Meeting not found"));

        MeetingParticipant meetingParticipant = meetingParticipantRepository.findByMeetingAndUser(meeting, user)
                .orElseThrow(() -> new RuntimeException("참가 기록이 없습니다"));
        if(!meetingParticipant.isHost()) {
            throw new IllegalStateException("종료 권한이 없습니다");
        }

        meeting.setEnd(true);
        meetingRepository.save(meeting);

    }

    public void delegateHost(User fromUser, String toUser_id, String meeting_id) {
        Meeting meeting = meetingRepository.findById(meeting_id)
                .orElseThrow(MeetingNotFoundException::new);

        MeetingParticipant currentHost = meetingParticipantRepository.findByMeetingAndUser(meeting, fromUser)
                .orElseThrow(() -> new RuntimeException("모임 참여자가 아닙니다"));
        if(!currentHost.isHost()) {
            throw new IllegalStateException("권한 없음: 리더만 위임 가능");
        }

        //위임 대상자가 모임 참여자인지 확인
        User toUser = userRepository.findById(toUser_id)
                .orElseThrow(() -> new RuntimeException("대상자를 찾을 수 없습니다"));
        MeetingParticipant targetHost = meetingParticipantRepository.findByMeetingAndUser(meeting, toUser)
                .orElseThrow(() -> new RuntimeException("위임 대상자가 모임에 참여하지 않았습니다"));

        //호스트 교체
        currentHost.setHost(false);
        targetHost.setHost(true);
        meetingParticipantRepository.save(currentHost);
        meetingParticipantRepository.save(targetHost);
    }

    public List<MeetingResponse> findMyMeetings(User user, Boolean active) {
        List<MeetingParticipant> participants = meetingParticipantRepository.findAllByUser(user);
        List<MeetingResponse> result = new ArrayList<>();
        //디버깅용
        for(MeetingParticipant participant: participants) {
            log.info("participant={}, meeting={}", participant.getId(), participant.getMeeting());
            System.out.println("참가자!!!!!!!!!!"+participant);
        }
            //end

        for(MeetingParticipant participant: participants) {
            Meeting meeting = participant.getMeeting();

            // null check 먼저!
            if (meeting == null || meeting.getEvent() == null) {
                log.warn("Skipping participant [{}] due to null meeting or null event", participant.getId());
                continue;
            }


            // active 조건 처리
            if (Boolean.TRUE.equals(active) && meeting.isEnd()) {
                continue; // 활성화된 모임만 보고 싶은데 종료된 모임이면 건너뜀
            }

            if (Boolean.FALSE.equals(active) && !meeting.isEnd()) {
                continue; // 종료된 모임만 보고 싶은데 아직 진행 중이면 건너뜀
            }

            MeetingResponse meetingResponse = MeetingResponse.builder()
                    .id(meeting.getId())
                    .title(meeting.getTitle())
                    .description(meeting.getDescription())
                    .date(meeting.getDate())
                    .maxPeople(meeting.getMaxPeople())
                    .minAge(meeting.getMinAge())
                    .maxAge(meeting.getMaxAge())
                    .gender(meeting.getGender())
                    .isEnd(meeting.isEnd())
                    .isHost(participant.isHost())
                    .eventId(meeting.getEvent().getId())
                    .eventTitle(meeting.getEvent().getTitle())
                    .eventImage(meeting.getEvent().getImage())
                    .build();

            result.add(meetingResponse);
        }
        return result;
    }



}
