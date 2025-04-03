package com.github.kmu_wink.seoul_in_culture.domain.user.service;

import com.github.kmu_wink.seoul_in_culture.domain.user.dto.UserEditRequest;
import com.github.kmu_wink.seoul_in_culture.domain.user.dto.UserEditResponse;
import com.github.kmu_wink.seoul_in_culture.domain.user.exception.UserNotFoundException;
import com.github.kmu_wink.seoul_in_culture.domain.user.repository.UserRepository;
import com.github.kmu_wink.seoul_in_culture.domain.user.schema.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public UserEditResponse editUser(String userId, UserEditRequest request) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        user.setAvatar(request.avatar());
        user.setNickname(request.nickname());
        user.setDistrict(request.district());
        user.setMeetingOpen(request.meetingOpen());

        User editedUser = userRepository.save(user);

        return UserEditResponse.builder()
                .user(editedUser)
                .build();
    }
}
