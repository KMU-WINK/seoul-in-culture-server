package com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.$review.repository;

import com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.$review.schema.MeetingReview;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MeetingReviewRepository extends MongoRepository<MeetingReview, String> {
    List<MeetingReview> findAllByTargetUserId(String userId);
    List<MeetingReview> findTop2ByTargetUserIdOrderByCreatedAtDesc(String userId);
}
