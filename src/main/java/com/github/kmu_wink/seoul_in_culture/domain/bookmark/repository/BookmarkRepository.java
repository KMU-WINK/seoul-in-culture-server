package com.github.kmu_wink.seoul_in_culture.domain.bookmark.repository;

import com.github.kmu_wink.seoul_in_culture.domain.bookmark.schema.Bookmark;
import com.github.kmu_wink.seoul_in_culture.domain.event.schema.Event;
import com.github.kmu_wink.seoul_in_culture.domain.user.schema.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookmarkRepository extends MongoRepository<Bookmark, String> {

    List<Bookmark> findTop2ByUserOrderByCreatedAtDesc(User user);

    int countByUser(User user);

    List<Bookmark> findByUser(User user);

    boolean existsByUserAndEvent(User user, Event event);

    Optional<Bookmark> findByUserAndEvent(User user, Event event);
}
