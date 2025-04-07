package com.github.kmu_wink.seoul_in_culture.domain.user.service;

import com.github.kmu_wink.seoul_in_culture.common.auth.AuthGuard;
import com.github.kmu_wink.seoul_in_culture.domain.event.$bookmark.repository.BookmarkRepository;
import com.github.kmu_wink.seoul_in_culture.domain.event.$bookmark.schema.Bookmark;
import com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.$participant.repository.MeetingParticipantRepository;
import com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.$participant.schema.MeetingParticipant;
import com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.$review.repository.MeetingReviewRepository;
import com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.$review.schema.MeetingReview;
import com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.schema.Meeting;
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

    @AuthGuard
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

    @AuthGuard
    public MyDetailResponse getMyDetail(User user) {

        List<Bookmark> bookmarks = bookmarkRepository.findTop2ByUserOrderByCreatedAtDesc(user);

        int joinedMeetings = meetingParticipantRepository.countByUserAndHostFalse(user);
        int hostedMeetings = meetingParticipantRepository.countByUserAndHostTrue(user);


        List<MeetingReview> meetingReviews = meetingParticipantRepository.findAllByUser(user)
                .stream()
                .flatMap(p -> meetingReviewRepository.findAllByTarget(p).stream())
                .limit(2)
                .toList();

        return MyDetailResponse.builder()
                .user(user)
                .bookmark(bookmarks)
                .joinedMeeting(joinedMeetings)
                .hostedMeeting(hostedMeetings)
                .review(meetingReviews)
                .build();
    }

    public OtherDetailResponse getOtherDetail(String userId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        int bookmarkCount = bookmarkRepository.countByUser(user);
        int joinedMeetings = meetingParticipantRepository.countByUserAndHostFalse(user);

        List<MeetingReview> meetingReviews = meetingParticipantRepository.findAllByUser(user)
                .stream()
                .flatMap(p -> meetingReviewRepository.findAllByTarget(p).stream())
                .limit(2)
                .toList();

        List<Meeting> hostedParticipants = meetingParticipantRepository.findAllByUserAndHostTrue(user)
                .stream()
                .map(MeetingParticipant::getMeeting)
                .toList();


        return OtherDetailResponse.builder()
                .user(user)
                .bookmark(bookmarkCount)
                .joinedMeeting(joinedMeetings)
                .review(meetingReviews)
                .hostedMeeting(hostedParticipants)
                .build();
    }
}
