package com.github.kmu_wink.seoul_in_culture.domain.user.service;

import com.github.kmu_wink.seoul_in_culture.domain.event.$bookmark.repository.BookmarkRepository;
import com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.$review.repository.ReviewRepository;
import com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.repository.MeetingRepository;
import com.github.kmu_wink.seoul_in_culture.domain.user.dto.request.UserEditRequest;
import com.github.kmu_wink.seoul_in_culture.domain.user.dto.response.GetMyInfoResponse;
import com.github.kmu_wink.seoul_in_culture.domain.user.dto.response.GetOtherInfoResponse;
import com.github.kmu_wink.seoul_in_culture.domain.user.dto.response.UpdateMyInfoResponse;
import com.github.kmu_wink.seoul_in_culture.domain.user.exception.UserException;
import com.github.kmu_wink.seoul_in_culture.domain.user.repository.UserRepository;
import com.github.kmu_wink.seoul_in_culture.domain.user.schema.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.github.kmu_wink.seoul_in_culture.domain.user.exception.UserExceptions.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final MeetingRepository meetingRepository;
    private final BookmarkRepository bookmarkRepository;
    private final ReviewRepository reviewRepository;

    public GetMyInfoResponse getMyInfo(User user) {

        return GetMyInfoResponse.builder()
                .user(user)
                .bookmark(bookmarkRepository.findTop2ByUserOrderByCreatedAtDesc(user))
                .joinedMeeting(meetingRepository.countByParticipantsContaining(user))
                .hostedMeeting(meetingRepository.countByHost(user))
                .review(reviewRepository.findTop2ByTarget(user))
                .build();
    }

    public GetOtherInfoResponse getOtherInfo(String userId) {

        User user = userRepository.findById(userId).orElseThrow(() -> UserException.of(USER_NOT_FOUND));

        return GetOtherInfoResponse.builder()
                .user(user)
                .bookmark(bookmarkRepository.countByUser(user))
                .joinedMeeting(meetingRepository.countByParticipantsContaining(user))
                .hostedMeeting(meetingRepository.findAllByHost(user))
                .review(reviewRepository.findTop2ByTarget(user))
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
