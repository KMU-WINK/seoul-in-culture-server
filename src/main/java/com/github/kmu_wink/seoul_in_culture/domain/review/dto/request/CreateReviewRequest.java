package com.github.kmu_wink.seoul_in_culture.domain.review.dto.request;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record CreateReviewRequest(

        @Min(0)
        @Max(5)
        int score,

        @Nullable
        String content
) {

}
