package com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.$review.repository;

import com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.$review.schema.MeetingReview;
import com.github.kmu_wink.seoul_in_culture.domain.user.schema.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MeetingReviewRepository extends MongoRepository<MeetingReview, String> {
    List<MeetingReview> findTop2ByTargetUserOrderByCreatedAtDesc(User user);
}
