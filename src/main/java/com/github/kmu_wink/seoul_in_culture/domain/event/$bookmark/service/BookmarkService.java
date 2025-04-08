package com.github.kmu_wink.seoul_in_culture.domain.event.$bookmark.service;

import com.github.kmu_wink.seoul_in_culture.domain.event.$bookmark.dto.response.GetBookmarkResponse;
import com.github.kmu_wink.seoul_in_culture.domain.event.$bookmark.repository.BookmarkRepository;
import com.github.kmu_wink.seoul_in_culture.domain.event.$bookmark.schema.Bookmark;
import com.github.kmu_wink.seoul_in_culture.domain.user.schema.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;

    public List<GetBookmarkResponse> getBookmark(User user) {
        List<Bookmark> bookmarks = bookmarkRepository.findByUser(user);

        return bookmarks.stream()
                .map(b -> GetBookmarkResponse.builder()
                        .event(b.getEvent())
                        .build())
                .toList();
    }
}
