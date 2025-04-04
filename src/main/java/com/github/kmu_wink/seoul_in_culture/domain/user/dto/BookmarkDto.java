package com.github.kmu_wink.seoul_in_culture.domain.user.dto;

import com.github.kmu_wink.seoul_in_culture.domain.event.schema.Event;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record BookmarkDto(
        String id,
        Event.Category category,
        String image,
        String title,
        LocalDateTime startDate,
        LocalDateTime endDate
) {
}
