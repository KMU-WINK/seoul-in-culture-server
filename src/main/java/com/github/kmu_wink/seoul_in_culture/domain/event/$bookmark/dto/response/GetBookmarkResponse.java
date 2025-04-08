package com.github.kmu_wink.seoul_in_culture.domain.event.$bookmark.dto.response;

import com.github.kmu_wink.seoul_in_culture.domain.event.schema.Event;
import lombok.Builder;

@Builder
public record GetBookmarkResponse(
        Event event
) {
}
