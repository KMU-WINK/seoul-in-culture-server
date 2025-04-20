package com.github.kmu_wink.seoul_in_culture.domain.review.dto.response;

import com.github.kmu_wink.seoul_in_culture.domain.review.schema.Review;
import lombok.Builder;

@Builder
public record GetReviewResponse(

        Review review
) {

}
