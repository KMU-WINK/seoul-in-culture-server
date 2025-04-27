package com.github.kmu_wink.seoul_in_culture.domain.chat.repository;

import com.github.kmu_wink.seoul_in_culture.domain.chat.schema.Chat;
import com.github.kmu_wink.seoul_in_culture.domain.meeting.schema.Meeting;
import com.github.kmu_wink.seoul_in_culture.domain.user.schema.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRepository extends MongoRepository<Chat, String> {

    Optional<Chat> findTopByMeeting(Meeting meeting);

    List<Chat> findAllByMeeting(Meeting meeting);

    int countByMeetingAndUnreadContaining(Meeting meeting, User user);
}
