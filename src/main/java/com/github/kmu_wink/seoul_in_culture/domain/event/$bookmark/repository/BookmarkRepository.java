package com.github.kmu_wink.seoul_in_culture.domain.event.$bookmark.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.github.kmu_wink.seoul_in_culture.domain.event.$bookmark.schema.Bookmark;
import com.github.kmu_wink.seoul_in_culture.domain.user.schema.User;

@Repository
public interface BookmarkRepository extends MongoRepository<Bookmark, String> {

    List<Bookmark> findTop2ByUserOrderByCreatedAtDesc(User userId);

    int countByUser(User user);
}
