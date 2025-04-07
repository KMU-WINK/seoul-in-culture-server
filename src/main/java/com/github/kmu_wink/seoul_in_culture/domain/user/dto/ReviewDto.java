package com.github.kmu_wink.seoul_in_culture.domain.user.dto;

import com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.$review.schema.MeetingReview;
import lombok.Builder;

@Builder
public record ReviewDto(
        MeetingReview review
) {
}
