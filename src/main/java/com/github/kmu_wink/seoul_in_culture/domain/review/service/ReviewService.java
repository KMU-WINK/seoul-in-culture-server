package com.github.kmu_wink.seoul_in_culture.domain.review.service;

import com.github.kmu_wink.seoul_in_culture.domain.meeting.exception.MeetingException;
import com.github.kmu_wink.seoul_in_culture.domain.meeting.repository.MeetingRepository;
import com.github.kmu_wink.seoul_in_culture.domain.meeting.schema.Meeting;
import com.github.kmu_wink.seoul_in_culture.domain.notification.api.NotificationApi;
import com.github.kmu_wink.seoul_in_culture.domain.notification.schema.detail.MeetingReviewDetail;
import com.github.kmu_wink.seoul_in_culture.domain.review.dto.request.CreateReviewRequest;
import com.github.kmu_wink.seoul_in_culture.domain.review.dto.response.GetReviewResponse;
import com.github.kmu_wink.seoul_in_culture.domain.review.dto.response.GetReviewsResponse;
import com.github.kmu_wink.seoul_in_culture.domain.review.exception.ReviewException;
import com.github.kmu_wink.seoul_in_culture.domain.review.repository.ReviewRepository;
import com.github.kmu_wink.seoul_in_culture.domain.review.schema.Review;
import com.github.kmu_wink.seoul_in_culture.domain.user.exception.UserException;
import com.github.kmu_wink.seoul_in_culture.domain.user.repository.UserRepository;
import com.github.kmu_wink.seoul_in_culture.domain.user.schema.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.github.kmu_wink.seoul_in_culture.domain.meeting.exception.MeetingExceptions.MEETING_NOT_FOUND;
import static com.github.kmu_wink.seoul_in_culture.domain.meeting.exception.MeetingExceptions.MEETING_NOT_JOINED;
import static com.github.kmu_wink.seoul_in_culture.domain.review.exception.ReviewExceptions.ALREADY_REVIEW;
import static com.github.kmu_wink.seoul_in_culture.domain.review.exception.ReviewExceptions.TARGET_NOT_PARTICIPANT_MEETING;
import static com.github.kmu_wink.seoul_in_culture.domain.user.exception.UserExceptions.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final UserRepository userRepository;
    private final MeetingRepository meetingRepository;
    private final ReviewRepository reviewRepository;

	private final NotificationApi notificationApi;

    public GetReviewsResponse getReviews(User user) {

        List<Review> reviews = reviewRepository.findAllByTarget(user);

        return GetReviewsResponse.builder()
                .reviews(reviews)
                .build();
    }

    public GetReviewsResponse getReview(User user, String meetingId) {

        Meeting meeting = meetingRepository.findById(meetingId)
                .stream()
                .peek(x -> {
                    if (!x.getParticipants().contains(user))
                        throw MeetingException.of(MEETING_NOT_JOINED);
                })
                .findFirst()
                .orElseThrow(() -> MeetingException.of(MEETING_NOT_FOUND));

        List<Review> reviews = reviewRepository.findAllByMeetingAndAuthor(meeting, user);

        return GetReviewsResponse.builder()
                .reviews(reviews)
                .build();
    }

    public GetReviewResponse createReview(User user, String meetingId, String targetUserId, CreateReviewRequest dto) {

        Meeting meeting = meetingRepository.findById(meetingId).stream()
                .peek(x -> {
                    if (!x.getParticipants().contains(user))
                        throw MeetingException.of(MEETING_NOT_JOINED);
                })
                .findFirst()
                .orElseThrow(() -> MeetingException.of(MEETING_NOT_FOUND));

        User targetUser = userRepository.findById(targetUserId).stream()
                .peek(x -> {
                    if (!meeting.getParticipants().contains(x))
                        throw ReviewException.of(TARGET_NOT_PARTICIPANT_MEETING);
                })
                .findFirst()
                .orElseThrow(() -> UserException.of(USER_NOT_FOUND));

        if (reviewRepository.existsByMeetingAndAuthorAndTarget(meeting, user, targetUser))
            throw ReviewException.of(ALREADY_REVIEW);

        Review review = Review.builder()
                .meeting(meeting)
                .author(user)
                .target(targetUser)
                .score(dto.score())
                .content(dto.content())
                .build();

        review = reviewRepository.save(review);

		notificationApi.sendNotification(
				targetUser,
				MeetingReviewDetail.builder()
						.meeting(meeting)
						.user(user)
						.build()
		);

        return GetReviewResponse.builder()
                .review(review)
                .build();
    }
}
