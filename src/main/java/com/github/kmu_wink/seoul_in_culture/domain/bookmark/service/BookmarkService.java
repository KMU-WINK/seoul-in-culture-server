package com.github.kmu_wink.seoul_in_culture.domain.bookmark.service;

import com.github.kmu_wink.seoul_in_culture.domain.bookmark.dto.response.GetBookmarkResponse;
import com.github.kmu_wink.seoul_in_culture.domain.bookmark.repository.BookmarkRepository;
import com.github.kmu_wink.seoul_in_culture.domain.bookmark.schema.Bookmark;
import com.github.kmu_wink.seoul_in_culture.domain.event.exception.EventException;
import com.github.kmu_wink.seoul_in_culture.domain.event.repository.EventRepository;
import com.github.kmu_wink.seoul_in_culture.domain.event.schema.Event;
import com.github.kmu_wink.seoul_in_culture.domain.user.schema.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.github.kmu_wink.seoul_in_culture.domain.event.exception.EventExceptions.EVENT_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final EventRepository eventRepository;

    public GetBookmarkResponse getBookmark(User user) {

        List<Event> eventList = bookmarkRepository.findByUser(user).stream()
                .map(Bookmark::getEvent)
                .toList();

        return GetBookmarkResponse.builder()
                .bookmark(eventList)
                .build();
    }

    public void postBookmark(User user, String eventId) {

        Event event = eventRepository.findById(eventId).orElseThrow(() -> EventException.of(EVENT_NOT_FOUND));

        bookmarkRepository.findByUserAndEvent(user, event)
                .orElseGet(() ->
                        bookmarkRepository.save(
                                Bookmark.builder()
                                        .user(user)
                                        .event(event)
                                        .build()
                        )
                );
    }

    public void deleteBookmark(User user, String eventId) {

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> EventException.of(EVENT_NOT_FOUND));

        bookmarkRepository.findByUserAndEvent(user, event).ifPresent(bookmarkRepository::delete);
    }
}
