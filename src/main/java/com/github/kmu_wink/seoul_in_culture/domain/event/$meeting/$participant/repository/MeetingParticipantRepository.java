package com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.$participant.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.$participant.schema.MeetingParticipant;
import com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.schema.Meeting;
import com.github.kmu_wink.seoul_in_culture.domain.user.schema.User;

@Repository
public interface MeetingParticipantRepository extends MongoRepository<MeetingParticipant, String> {

	List<MeetingParticipant> findAllByUser(User user);
	List<MeetingParticipant> findAllByMeeting(Meeting meeting);

	Optional<MeetingParticipant> findByMeetingAndUser(Meeting meeting, User user);

	boolean existsByMeetingAndUser(Meeting meeting, User user);

	void deleteByMeetingAndUser(Meeting meeting, User user);

	void deleteAllByMeetingId(String meetingId);
}