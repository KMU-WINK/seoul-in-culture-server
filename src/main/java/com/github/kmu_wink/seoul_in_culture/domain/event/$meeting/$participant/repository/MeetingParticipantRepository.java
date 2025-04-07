package com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.$participant.repository;

import com.github.kmu_wink.seoul_in_culture.domain.user.schema.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.$participant.schema.MeetingParticipant;

import java.util.List;

@Repository
public interface MeetingParticipantRepository extends MongoRepository<MeetingParticipant, String> {
    int countByUserAndHostFalse(User user);
    int countByUserAndHostTrue(User user);

    List<MeetingParticipant> findAllByUserAndHostTrue(User user);
}
