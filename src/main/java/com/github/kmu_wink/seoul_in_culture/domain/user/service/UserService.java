package com.github.kmu_wink.seoul_in_culture.domain.user.service;

import com.github.kmu_wink.seoul_in_culture.domain.event.$bookmark.repository.BookmarkRepository;
import com.github.kmu_wink.seoul_in_culture.domain.event.$bookmark.schema.Bookmark;
import com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.$participant.repository.MeetingParticipantRepository;
import com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.$review.repository.MeetingReviewRepository;
import com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.$review.schema.MeetingReview;
import com.github.kmu_wink.seoul_in_culture.domain.user.dto.*;
import com.github.kmu_wink.seoul_in_culture.domain.user.repository.UserRepository;
import com.github.kmu_wink.seoul_in_culture.domain.user.schema.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
                .id(user.getId())
                .nickname(user.getNickname())
                .createdAt(user.getCreatedAt())
                .email(user.getEmail())
                .district(user.getDistrict().toString())
                .score(getScore(user))
                .experience(user.getExperience())
                .gender(user.getGender().toString())
                .age(calculateAge(user.getBirthYear()))
                .build();

        List<Bookmark> bookmarks = bookmarkRepository.findTop2ByUserIdOrderByCreatedAtDesc(user.getId());
        List<BookmarkDto> bookmarkDtos = bookmarks.stream()
                .map(b -> BookmarkDto.builder()
                        .id(b.getId())
                        .category(b.getEvent().getCategory())
                        .image(b.getEvent().getImage())
                        .title(b.getEvent().getTitle())
                        .startDate(b.getEvent().getStartDate())
                        .endDate(b.getEvent().getEndDate())
                        .build())
                .toList();

        int joinedMeetings = meetingParticipantRepository.countByUserIdAndHostFalse(user.getId());
        int hostedMeetings = meetingParticipantRepository.countByUserIdAndHostTrue(user.getId());

        List<MeetingReview> meetingReviews = meetingReviewRepository.findTop2ByTargetUserIdOrderByCreatedAtDesc(user.getId());
        List<ReviewDto> reviewDtos = meetingReviews.stream()
                .map(r -> ReviewDto.builder()
                        .id(r.getId())
                        .author(
                                AuthorDto.builder()
                                        .createdAt(r.getAuthor().getCreatedAt())
                                        .avatar(r.getAuthor().getUser().getAvatar())
                                        .nickname(r.getAuthor().getUser().getNickname())
                                        .build()
                        )
                        .score(r.getScore())
                        .content(r.getContent())
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

    private double getScore(User user) {
        List<MeetingReview> meetingReviews = meetingReviewRepository.findAllByTargetUserId(user.getId());
        return meetingReviews.stream()
                .mapToInt(MeetingReview::getScore)
                .average()
                .orElse(2.5);
    }

    private int calculateAge(int birthYear) {
        return LocalDateTime.now().getYear() - birthYear;
    }
}
