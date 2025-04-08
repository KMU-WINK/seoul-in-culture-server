package com.github.kmu_wink.seoul_in_culture.domain.event.$bookmark.exception;

import com.github.kmu_wink.seoul_in_culture.common.api.ApiException;

public class BookmarkException extends ApiException {

    private BookmarkException(BookmarkExceptions bookmarkExceptions) {
        super(bookmarkExceptions.getMessage());
    }

    public static BookmarkException of(BookmarkExceptions bookmarkExceptions) {
        return new BookmarkException(bookmarkExceptions);
    }
}
