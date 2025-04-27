package com.github.kmu_wink.seoul_in_culture.domain.user.repository;

import com.github.kmu_wink.seoul_in_culture.domain.user.schema.User;

import java.util.Optional;

public interface UserRepository {

    Optional<User> findById(String id);
    Optional<User> findByKakao(long kakao);

    boolean existsByNickname(String nickname);

    User save(User user);
}
