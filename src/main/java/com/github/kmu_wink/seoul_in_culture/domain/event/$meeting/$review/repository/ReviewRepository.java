package com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.$review.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.$review.schema.Review;
import com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.schema.Meeting;
import com.github.kmu_wink.seoul_in_culture.domain.user.schema.User;

@Repository
public interface ReviewRepository extends MongoRepository<Review, String> {

    List<Review> findAllByMeetingAndAuthor(Meeting meeting, User user);
    List<Review> findAllByTarget(User user);
    List<Review> findTop2ByTarget(User user);

    boolean existsByMeetingAndAuthorAndTarget(Meeting meeting, User author, User target);

    void deleteAllByMeeting(Meeting meeting);
}
