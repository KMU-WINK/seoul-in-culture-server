package com.github.kmu_wink.seoul_in_culture.domain.chat.repository;

import com.github.kmu_wink.seoul_in_culture.domain.chat.schema.Chat;
import com.github.kmu_wink.seoul_in_culture.domain.meeting.schema.Meeting;
import com.github.kmu_wink.seoul_in_culture.domain.user.schema.User;

import java.util.List;
import java.util.Optional;

public interface ChatRepository {

    Optional<Chat> findById(String chattingId);
    Optional<Chat> findFirstByMeeting(Meeting meeting);

    List<Chat> findAllByMeeting(Meeting meeting);

    long countUnreadByMeeting(Meeting meeting, User user);

    void readAll(Meeting meeting, User user);

    Chat save(Chat chat);
}
