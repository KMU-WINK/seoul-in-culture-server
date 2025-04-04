package com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.$participant.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.$participant.schema.MeetingParticipant;

import java.util.List;

@Repository
public interface MeetingParticipantRepository extends MongoRepository<MeetingParticipant, String> {
    int countByUserIdAndHostFalse(String userId);
    int countByUserIdAndHostTrue(String userId);

    List<MeetingParticipant> findAllByUserIdAndHostTrue(String userId);
}
