package com.github.kmu_wink.seoul_in_culture.domain.user.service;

import com.github.kmu_wink.seoul_in_culture.domain.event.$bookmark.repository.BookmarkRepository;
import com.github.kmu_wink.seoul_in_culture.domain.event.$bookmark.schema.Bookmark;
import com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.$participant.repository.MeetingParticipantRepository;
import com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.$participant.schema.MeetingParticipant;
import com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.$review.repository.MeetingReviewRepository;
import com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.$review.schema.MeetingReview;
import com.github.kmu_wink.seoul_in_culture.domain.user.dto.*;
import com.github.kmu_wink.seoul_in_culture.domain.user.exception.UserNotFoundException;
import com.github.kmu_wink.seoul_in_culture.domain.user.repository.UserRepository;
import com.github.kmu_wink.seoul_in_culture.domain.user.schema.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final MeetingReviewRepository meetingReviewRepository;
    private final BookmarkRepository bookmarkRepository;
    private final MeetingParticipantRepository meetingParticipantRepository;

    @Transactional
    public UserEditResponse editUser(User user, UserEditRequest request) {
        user.setAvatar(request.avatar());
        user.setNickname(request.nickname());
        user.setDistrict(request.district());
        user.setMeetingOpen(request.meetingOpen());

        User editedUser = userRepository.save(user);

        return UserEditResponse.builder()
                .user(editedUser)
                .build();
    }

    public MyDetailResponse getMyDetail(User user) {
        UserDto userDto = UserDto.builder()
                .user(user)
                .build();

        List<Bookmark> bookmarks = bookmarkRepository.findTop2ByUserOrderByCreatedAtDesc(user);
        List<BookmarkDto> bookmarkDtos = bookmarks.stream()
                .map(b -> BookmarkDto.builder()
                        .bookmark(b)
                        .build())
                .toList();

        int joinedMeetings = meetingParticipantRepository.countByUserAndHostFalse(user);
        int hostedMeetings = meetingParticipantRepository.countByUserAndHostTrue(user);

        List<MeetingReview> meetingReviews = meetingReviewRepository.findTop2ByTargetUserOrderByCreatedAtDesc(user);
        List<ReviewDto> reviewDtos = meetingReviews.stream()
                .map(r -> ReviewDto.builder()
                        .review(r)
                        .build())
                .toList();

        return MyDetailResponse.builder()
                .user(userDto)
                .bookmark(bookmarkDtos)
                .joinedMeeting(joinedMeetings)
                .hostedMeeting(hostedMeetings)
                .review(reviewDtos)
                .build();
    }

    public OtherDetailResponse getOtherDetail(String userId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        UserDto userDto = UserDto.builder()
                .user(user)
                .build();

        int bookmarkCount = bookmarkRepository.countByUser(user);
        int joinedMeetings = meetingParticipantRepository.countByUserAndHostFalse(user);

        List<MeetingReview> recentReviews = meetingReviewRepository.findTop2ByTargetUserOrderByCreatedAtDesc(user);
        List<ReviewDto> reviewDtos = recentReviews.stream()
                .map(r -> ReviewDto.builder()
                        .review(r)
                        .build())
                .toList();

        List<MeetingParticipant> hostedParticipants = meetingParticipantRepository.findAllByUserAndHostTrue(user);
        List<MeetingDto> hostedMeetingDtos = hostedParticipants.stream()
                .map(mp -> MeetingDto.builder()
                        .meetingParticipant(mp)
                        .build())
                .toList();

        return OtherDetailResponse.builder()
                .user(userDto)
                .bookmark(bookmarkCount)
                .joinedMeeting(joinedMeetings)
                .review(reviewDtos)
                .hostedMeeting(hostedMeetingDtos)
                .build();
    }
}
