package com.github.kmu_wink.seoul_in_culture.domain.notification.repository;

import com.github.kmu_wink.seoul_in_culture.domain.notification.schema.FcmToken;
import com.github.kmu_wink.seoul_in_culture.domain.user.schema.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FcmTokenRepository extends MongoRepository<FcmToken, String> {

    Optional<FcmToken> findByUser(User user);

    void deleteByToken(String token);
}
