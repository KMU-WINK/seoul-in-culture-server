package com.github.kmu_wink.seoul_in_culture.domain.bookmark.repository;

import com.github.kmu_wink.seoul_in_culture.domain.bookmark.schema.Bookmark;
import com.github.kmu_wink.seoul_in_culture.domain.event.schema.Event;
import com.github.kmu_wink.seoul_in_culture.domain.user.schema.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookmarkRepository extends MongoRepository<Bookmark, String> {

    List<Bookmark> findAllByUser(User user, Sort sort);
    List<Bookmark> findTop2ByUser(User user, Sort sort);

    int countByUser(User user);

    boolean existsByUserAndEvent(User user, Event event);

    void deleteByUserAndEvent(User user, Event event);
}
