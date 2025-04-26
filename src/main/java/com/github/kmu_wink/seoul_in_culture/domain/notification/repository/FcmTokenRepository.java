package com.github.kmu_wink.seoul_in_culture.domain.notification.repository;

import com.github.kmu_wink.seoul_in_culture.domain.notification.schema.FcmToken;
import com.github.kmu_wink.seoul_in_culture.domain.user.schema.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FcmTokenRepository extends MongoRepository<FcmToken, String> {

    List<FcmToken> findAllByUser(User user);

    Optional<FcmToken> findByToken(String token);

    boolean existsByToken(String token);

    void deleteByToken(String token);
}
