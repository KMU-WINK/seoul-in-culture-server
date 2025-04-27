package com.github.kmu_wink.seoul_in_culture.domain.user.service;

import com.github.kmu_wink.seoul_in_culture.common.s3.S3Service;
import com.github.kmu_wink.seoul_in_culture.domain.bookmark.repository.BookmarkRepository;
import com.github.kmu_wink.seoul_in_culture.domain.bookmark.schema.Bookmark;
import com.github.kmu_wink.seoul_in_culture.domain.meeting.repository.MeetingRepository;
import com.github.kmu_wink.seoul_in_culture.domain.review.repository.ReviewRepository;
import com.github.kmu_wink.seoul_in_culture.domain.review.schema.Review;
import com.github.kmu_wink.seoul_in_culture.domain.user.dto.request.UserEditRequest;
import com.github.kmu_wink.seoul_in_culture.domain.user.dto.response.GetMyInfoResponse;
import com.github.kmu_wink.seoul_in_culture.domain.user.dto.response.GetOtherInfoResponse;
import com.github.kmu_wink.seoul_in_culture.domain.user.dto.response.UpdateMyInfoResponse;
import com.github.kmu_wink.seoul_in_culture.domain.user.exception.UserException;
import com.github.kmu_wink.seoul_in_culture.domain.user.repository.UserRepository;
import com.github.kmu_wink.seoul_in_culture.domain.user.schema.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.UUID;

import static com.github.kmu_wink.seoul_in_culture.common.mongo.MongoConfig.LATEST_SORT;
import static com.github.kmu_wink.seoul_in_culture.domain.user.exception.UserExceptions.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final MeetingRepository meetingRepository;
    private final BookmarkRepository bookmarkRepository;
    private final ReviewRepository reviewRepository;

    private final S3Service s3Service;

    public GetMyInfoResponse getMyInfo(User user) {

        return GetMyInfoResponse.builder()
                .user(user)
                .bookmarks(bookmarkRepository.findTop2ByUser(user, LATEST_SORT)
                        .stream()
                        .map(Bookmark::getEvent)
                        .toList())
                .joinedMeetings(meetingRepository.countByParticipantsContaining(user))
                .hostedMeetings(meetingRepository.findAllByHost(user, LATEST_SORT))
                .reviews(reviewRepository.findTop2ByTarget(user, LATEST_SORT))
                .score(reviewRepository.findAllByTarget(user, LATEST_SORT).stream().mapToInt(Review::getScore).average().orElse(0))
                .build();
    }

    public GetOtherInfoResponse getOtherInfo(String userId) {

        User user = userRepository.findById(userId).orElseThrow(() -> UserException.of(USER_NOT_FOUND));

        return GetOtherInfoResponse.builder()
                .user(user)
                .bookmarks(bookmarkRepository.countByUser(user))
                .joinedMeetings(meetingRepository.countByParticipantsContaining(user))
                .hostedMeetings(meetingRepository.findAllByHost(user, LATEST_SORT))
                .reviews(reviewRepository.findTop2ByTarget(user, LATEST_SORT))
                .score(reviewRepository.findAllByTarget(user, LATEST_SORT).stream().mapToInt(Review::getScore).average().orElse(0))
                .build();
    }

    public UpdateMyInfoResponse updateMyInfo(User user, MultipartFile avatar, UserEditRequest dto) {

        if (avatar != null) {
            if (user.getAvatar() != null) {
                s3Service.urlToKey(user.getAvatar()).ifPresent(s3Service::deleteFile);
            }

            user.setAvatar(s3Service.upload("avatar/" + UUID.randomUUID(), avatar));
        }

        user.setNickname(dto.nickname());
        user.setDistrict(dto.district());

        if (user.getGender() == null) {
            user.setGender(dto.gender());
        }

        if (user.getBirthYear() == null) {
            user.setBirthYear(LocalDate.now().getYear() - dto.age() + 1);
        }

        user = userRepository.save(user);

        return UpdateMyInfoResponse.builder().user(user).build();
    }
}
