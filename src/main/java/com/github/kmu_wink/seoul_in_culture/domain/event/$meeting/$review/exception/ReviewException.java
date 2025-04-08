package com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.$review.exception;

import com.github.kmu_wink.seoul_in_culture.common.api.ApiException;

public class ReviewException extends ApiException {

	private ReviewException(ReviewExceptions reviewExceptions) {

		super(reviewExceptions.getMessage());
	}

	public static ReviewException of(ReviewExceptions reviewExceptions) {

		return new ReviewException(reviewExceptions);
	}
}
