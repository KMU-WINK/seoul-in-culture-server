package com.github.kmu_wink.seoul_in_culture.domain.user.service;

import static com.github.kmu_wink.seoul_in_culture.domain.user.exception.UserExceptions.*;

import java.util.List;

import org.springframework.stereotype.Service;

import com.github.kmu_wink.seoul_in_culture.domain.event.$bookmark.repository.BookmarkRepository;
import com.github.kmu_wink.seoul_in_culture.domain.event.$bookmark.schema.Bookmark;
import com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.$review.repository.MeetingReviewRepository;
import com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.$review.schema.MeetingReview;
import com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.repository.MeetingRepository;
import com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.schema.Meeting;
import com.github.kmu_wink.seoul_in_culture.domain.user.dto.request.UserEditRequest;
import com.github.kmu_wink.seoul_in_culture.domain.user.dto.response.GetMyInfoResponse;
import com.github.kmu_wink.seoul_in_culture.domain.user.dto.response.GetOtherInfoResponse;
import com.github.kmu_wink.seoul_in_culture.domain.user.dto.response.UpdateMyInfoResponse;
import com.github.kmu_wink.seoul_in_culture.domain.user.exception.UserException;
import com.github.kmu_wink.seoul_in_culture.domain.user.repository.UserRepository;
import com.github.kmu_wink.seoul_in_culture.domain.user.schema.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final MeetingRepository meetingRepository;
    private final BookmarkRepository bookmarkRepository;
    private final MeetingReviewRepository meetingReviewRepository;

    public GetMyInfoResponse getMyInfo(User user) {

        List<Bookmark> bookmark = bookmarkRepository.findTop2ByUserOrderByCreatedAtDesc(user);

        int joinedMeeting = meetingRepository.countByParticipantsContainingAndHostIsFalse(user);
        int hostedMeeting = meetingRepository.countByParticipantsContainingAndHostIsTrue(user);

        List<MeetingReview> review = meetingReviewRepository.findTop2ByTarget(user);

        return GetMyInfoResponse.builder()
            .user(user)
            .bookmark(bookmark)
            .joinedMeeting(joinedMeeting)
            .hostedMeeting(hostedMeeting)
            .review(review)
            .build();
    }

    public GetOtherInfoResponse getOtherInfo(String userId) {

        User user = userRepository.findById(userId).orElseThrow(() -> UserException.of(USER_NOT_FOUND));

        int bookmark = bookmarkRepository.countByUser(user);

        int joinedMeeting = meetingRepository.countByParticipantsContainingAndHostIsFalse(user);
        List<Meeting> hostedMeeting = meetingRepository.findAllByParticipantsContainingAndHostIsTrue(user);

        List<MeetingReview> review = meetingReviewRepository.findTop2ByTarget(user);

        return GetOtherInfoResponse.builder()
            .user(user)
            .bookmark(bookmark)
            .joinedMeeting(joinedMeeting)
            .hostedMeeting(hostedMeeting)
            .review(review)
            .build();
    }

    public UpdateMyInfoResponse updateMyInfo(User user, UserEditRequest dto) {

        user.setAvatar(dto.avatar());
        user.setNickname(dto.nickname());
        user.setDistrict(dto.district());
        user.setMeetingOpen(dto.meetingOpen());

        user = userRepository.save(user);

        return UpdateMyInfoResponse.builder()
            .user(user)
            .build();
    }
}
