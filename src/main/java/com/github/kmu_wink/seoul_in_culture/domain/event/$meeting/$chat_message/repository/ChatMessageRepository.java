package com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.$chat_message.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.$chat_message.schema.ChatMessage;

@Repository
public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {
}
