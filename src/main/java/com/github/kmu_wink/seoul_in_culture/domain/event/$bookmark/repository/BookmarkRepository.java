package com.github.kmu_wink.seoul_in_culture.domain.event.$bookmark.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.github.kmu_wink.seoul_in_culture.domain.event.$bookmark.schema.Bookmark;

@Repository
public interface BookmarkRepository extends MongoRepository<Bookmark, String> {
}
