package com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.$review.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.$review.schema.MeetingReview;
import com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.schema.Meeting;
import com.github.kmu_wink.seoul_in_culture.domain.user.schema.User;

@Repository
public interface MeetingReviewRepository extends MongoRepository<MeetingReview, String> {

    List<MeetingReview> findTop2ByTarget(User user);

    void deleteAllByMeeting(Meeting meeting);
}
