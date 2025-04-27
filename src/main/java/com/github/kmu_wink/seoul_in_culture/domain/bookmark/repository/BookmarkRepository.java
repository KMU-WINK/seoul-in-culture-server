package com.github.kmu_wink.seoul_in_culture.domain.bookmark.repository;

import com.github.kmu_wink.seoul_in_culture.domain.bookmark.schema.Bookmark;
import com.github.kmu_wink.seoul_in_culture.domain.event.schema.Event;
import com.github.kmu_wink.seoul_in_culture.domain.user.schema.User;

import java.util.List;

public interface BookmarkRepository {

    List<Event> findAllByUser(User user);
    List<Event> findTop2ByUser(User user);

    long countByUser(User user);

    boolean existsByUserAndEvent(User user, Event event);

    Event save(Bookmark bookmark);

    void deleteByUserAndEvent(User user, Event event);
}
