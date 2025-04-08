package com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.$review.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.kmu_wink.seoul_in_culture.common.api.ApiResponse;
import com.github.kmu_wink.seoul_in_culture.common.security.authentication.AuthGuard;
import com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.$review.dto.request.CreateReviewRequest;
import com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.$review.dto.response.GetReviewResponse;
import com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.$review.dto.response.GetReviewsResponse;
import com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.$review.service.ReviewService;
import com.github.kmu_wink.seoul_in_culture.domain.user.schema.User;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@AuthGuard
@RestController
@RequestMapping("/event")
@RequiredArgsConstructor
@Tag(name = "[행사] [모임] [후기]")
public class ReviewController {

	private final ReviewService reviewService;

	@GetMapping("/meeting/review")
	@Operation(summary = "받은 리뷰 조회")
	public ApiResponse<GetReviewsResponse> getReviews(
		@AuthenticationPrincipal User user
	) {

		return ApiResponse.ok(reviewService.getReviews(user));
	}

	@GetMapping("/meeting/{meetingId}/review")
	@Operation(summary = "특정 모임에서 다른 유저에게 한 리뷰 조회")
	public ApiResponse<GetReviewsResponse> getReview(
		@AuthenticationPrincipal User user,
		@PathVariable String meetingId
	) {

		return ApiResponse.ok(reviewService.getReview(user, meetingId));
	}

	@PostMapping("/meeting/{meetingId}/review/{targetUserId}")
	@Operation(summary = "리뷰 달기")
	public ApiResponse<GetReviewResponse> createReview(
		@AuthenticationPrincipal User user,
		@PathVariable String meetingId,
		@PathVariable String targetUserId,
		@RequestBody @Valid CreateReviewRequest request
	) {

		return ApiResponse.ok(reviewService.createReview(user, meetingId, targetUserId, request));
	}
}
