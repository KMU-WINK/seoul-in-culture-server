package com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.$chat_message.repository;

import com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.$chat_message.schema.ChatMessage;
import com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.schema.Meeting;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {

	List<ChatMessage> findAllByMeeting(Meeting meeting);

	Optional<ChatMessage> findLastByMeeting(Meeting meeting);

	void deleteAllByMeeting(Meeting meeting);
}
