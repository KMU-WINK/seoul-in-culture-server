package com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.$review.dto.request;

public record CreateReviewRequest(

	int score,
	String content
) {
}
