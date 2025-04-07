package com.github.kmu_wink.seoul_in_culture.domain.user.dto;

import com.github.kmu_wink.seoul_in_culture.domain.event.$bookmark.schema.Bookmark;
import lombok.Builder;

@Builder
public record BookmarkDto(
        Bookmark bookmark
) {
}
