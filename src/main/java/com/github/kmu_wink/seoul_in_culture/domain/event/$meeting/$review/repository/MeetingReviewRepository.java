package com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.$review.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.github.kmu_wink.seoul_in_culture.domain.notification.schema.detail.MeetingReview;

@Repository
public interface MeetingReviewRepository extends MongoRepository<MeetingReview, String> {
}
