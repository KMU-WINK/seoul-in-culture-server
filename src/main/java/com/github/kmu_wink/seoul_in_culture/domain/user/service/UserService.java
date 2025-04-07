package com.github.kmu_wink.seoul_in_culture.domain.user.service;

import static com.github.kmu_wink.seoul_in_culture.domain.user.exception.UserExceptions.*;

import org.springframework.stereotype.Service;

import com.github.kmu_wink.seoul_in_culture.domain.event.$bookmark.repository.BookmarkRepository;
import com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.$review.repository.MeetingReviewRepository;
import com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.repository.MeetingRepository;
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

        return GetMyInfoResponse.builder()
            .user(user)
            .bookmark(bookmarkRepository.findTop2ByUserOrderByCreatedAtDesc(user))
            .joinedMeeting(meetingRepository.countByParticipantsContainingAndHostIsFalse(user))
            .hostedMeeting(meetingRepository.countByParticipantsContainingAndHostIsTrue(user))
            .review(meetingReviewRepository.findTop2ByTarget(user))
            .build();
    }

    public GetOtherInfoResponse getOtherInfo(String userId) {

        User user = userRepository.findById(userId).orElseThrow(() -> UserException.of(USER_NOT_FOUND));

        return GetOtherInfoResponse.builder()
            .user(user)
            .bookmark(bookmarkRepository.countByUser(user))
            .joinedMeeting(meetingRepository.countByParticipantsContainingAndHostIsFalse(user))
            .hostedMeeting(meetingRepository.findAllByParticipantsContainingAndHostIsTrue(user))
            .review(meetingReviewRepository.findTop2ByTarget(user))
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
