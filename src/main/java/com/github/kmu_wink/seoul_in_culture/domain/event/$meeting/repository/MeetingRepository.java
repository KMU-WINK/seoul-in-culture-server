package com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.schema.Meeting;
import com.github.kmu_wink.seoul_in_culture.domain.user.schema.User;

@Repository
public interface MeetingRepository extends MongoRepository<Meeting, String> {

	List<Meeting> findAllByHost(User user);
	List<Meeting> findAllByParticipantsContaining(User user);

	int countByHost(User user);
	int countByParticipantsContaining(User user);
}
