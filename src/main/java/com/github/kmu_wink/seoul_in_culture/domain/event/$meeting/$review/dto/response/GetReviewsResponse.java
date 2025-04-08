package com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.$review.dto.response;

import java.util.Collection;

import com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.$review.schema.Review;

import lombok.Builder;

@Builder
public record GetReviewsResponse(

	Collection<Review> reviews
) {
}
