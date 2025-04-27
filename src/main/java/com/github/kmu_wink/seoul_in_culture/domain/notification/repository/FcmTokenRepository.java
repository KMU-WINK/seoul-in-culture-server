package com.github.kmu_wink.seoul_in_culture.domain.notification.repository;

import com.github.kmu_wink.seoul_in_culture.domain.notification.schema.FcmToken;
import com.github.kmu_wink.seoul_in_culture.domain.user.schema.User;

import java.util.List;
import java.util.Optional;

public interface FcmTokenRepository {

    List<String> findAllByUser(User user);

    Optional<FcmToken> findByToken(String token);

    boolean existsByToken(String token);

    FcmToken save(FcmToken fcmToken);

    void deleteByToken(String token);
}
