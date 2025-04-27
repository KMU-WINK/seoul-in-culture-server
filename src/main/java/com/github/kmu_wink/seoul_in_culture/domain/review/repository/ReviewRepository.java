package com.github.kmu_wink.seoul_in_culture.domain.review.repository;

import com.github.kmu_wink.seoul_in_culture.domain.meeting.schema.Meeting;
import com.github.kmu_wink.seoul_in_culture.domain.review.schema.Review;
import com.github.kmu_wink.seoul_in_culture.domain.user.schema.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends MongoRepository<Review, String> {

    List<Review> findAllByTarget(User user, Sort sort);
    List<Review> findAllByMeetingAndAuthor(Meeting meeting, User user, Sort sort);
    List<Review> findTop2ByTarget(User user, Sort sort);

    boolean existsByMeetingAndAuthorAndTarget(Meeting meeting, User author, User target);
}
