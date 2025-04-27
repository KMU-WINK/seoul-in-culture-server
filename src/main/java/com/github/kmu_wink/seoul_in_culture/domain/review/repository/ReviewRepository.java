package com.github.kmu_wink.seoul_in_culture.domain.review.repository;

import com.github.kmu_wink.seoul_in_culture.domain.meeting.schema.Meeting;
import com.github.kmu_wink.seoul_in_culture.domain.review.schema.Review;
import com.github.kmu_wink.seoul_in_culture.domain.user.schema.User;

import java.util.List;

public interface ReviewRepository {

    List<Review> findAllByTarget(User user);
    List<Review> findAllByMeetingAndAuthor(Meeting meeting, User user);
    List<Review> findTop2ByTarget(User user);

    boolean existsByMeetingAndAuthorAndTarget(Meeting meeting, User author, User target);

    Review save(Review review);
}
