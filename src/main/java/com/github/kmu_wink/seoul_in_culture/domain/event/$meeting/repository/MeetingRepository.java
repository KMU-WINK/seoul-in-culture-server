package com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.repository;

import com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.schema.Meeting;
import com.github.kmu_wink.seoul_in_culture.domain.user.schema.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MeetingRepository extends MongoRepository<Meeting, String> {

    List<Meeting> findAllByParticipantsContaining(User user);

    List<Meeting> findAllByParticipantsContainingAndEnd(User user, boolean end);

    List<Meeting> findAllByParticipantsContainingAndHostIsTrue(User user);

    int countByParticipantsContainingAndHostIsTrue(User user);

    int countByParticipantsContainingAndHostIsFalse(User user);
}
