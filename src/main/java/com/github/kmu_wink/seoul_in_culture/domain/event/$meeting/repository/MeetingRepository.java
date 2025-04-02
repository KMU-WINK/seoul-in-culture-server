package com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.schema.Meeting;

@Repository
public interface MeetingRepository extends MongoRepository<Meeting, String> {
}
