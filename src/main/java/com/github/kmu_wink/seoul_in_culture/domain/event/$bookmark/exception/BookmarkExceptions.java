package com.github.kmu_wink.seoul_in_culture.domain.event.$bookmark.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BookmarkExceptions {

    BOOKMARK_NOT_FOUND("북마크를 찾을 수 없습니다."),
    ;

    private final String message;
}
